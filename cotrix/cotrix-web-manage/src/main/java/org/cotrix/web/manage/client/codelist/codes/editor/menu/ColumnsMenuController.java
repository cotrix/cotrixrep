/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.menu;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.codes.editor.menu.ColumnsMenu.GroupsProvider;
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.SwitchGroupEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.Group;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ColumnsMenuController {
	
	interface ColumnsMenuControllerEventBinder extends EventBinder<ColumnsMenuController> {}
	
	private List<Group> activeGroups = new ArrayList<Group>();
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	@Inject
	private ManageServiceAsync managerService;
	
	@Inject
	private ColumnsMenu menu;
	
	@Inject
	@CodelistBus 
	private EventBus bus;
	
	private GroupsProvider groupsProvider = new GroupsProvider() {
		
		@Override
		public void getGroups(AsyncCallback<List<Group>> callback) {
			managerService.getGroups(codelist.getId(), callback);
		}
		
		@Override
		public List<Group> getActiveGroups() {
			return activeGroups;
		}
	};
	
	private SuccessCallback<List<Group>> callback = new SuccessCallback<List<Group>>() {
		
		@Override
		public void onSuccess(List<Group> result) {
			updateGroups(result);			
		}
	};
	
	public <T extends UIObject & HasClickHandlers> void bind(final T target) {
		target.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				menu.show(codelist, groupsProvider, callback, target);
			}
		});
	}
	
	@Inject
	void bind(ColumnsMenuControllerEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}
	
	@EventHandler
	void onGroupSwitched(GroupSwitchedEvent event) {
		Group group = event.getGroup();
		switch (event.getSwitchType()) {
			case TO_COLUMN: activeGroups.add(group); break;
			case TO_NORMAL: activeGroups.remove(group); break;
		}
	}
	
	private void updateGroups(List<Group> newActiveGroups) {
		Log.trace("updating groups");
		List<Group> toRemove = new ArrayList<Group>(activeGroups);
		for (Group group:toRemove) bus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_NORMAL));
		for (Group group:newActiveGroups) bus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_COLUMN));
	}
	
}
