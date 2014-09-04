/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.menu;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.GroupsSwitchedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.SwitchGroupsEvent;
import org.cotrix.web.manage.client.codelist.common.GroupFactory;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.Group;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
	
	private List<String> activeDefinitionIds;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	@Inject
	private ColumnsMenu menu;
	
	@Inject
	@CodelistBus 
	private EventBus bus;
	
	@Inject
	private GroupFactory factory;
	
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
				menu.show(codelist, factory.getUniqueGroups(), activeDefinitionIds, callback, target);
			}
		});
	}
	
	@Inject
	void bind(ColumnsMenuControllerEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}
	
	@EventHandler
	void onGroupSwitched(GroupSwitchedEvent  event) {
		Group group = event.getGroup();
		if (event.getSwitchType() == GroupSwitchType.TO_NORMAL) activeDefinitionIds.remove(group.getDefinition().getId());
	}
	
	@EventHandler
	void onGroupsSwitched(GroupsSwitchedEvent event) {
		if (activeDefinitionIds == null) activeDefinitionIds = new ArrayList<String>();
		else activeDefinitionIds.clear();
		
		for (Group group:event.getGroups()) if (!activeDefinitionIds.contains(group.getDefinition().getId())) activeDefinitionIds.add(group.getDefinition().getId());
	}
	
	private void updateGroups(List<Group> newActiveGroups) {
		Log.trace("updating groups");
		List<Group> groups = factory.expandGroups(newActiveGroups);
		bus.fireEvent(new SwitchGroupsEvent(groups));
	}	
}
