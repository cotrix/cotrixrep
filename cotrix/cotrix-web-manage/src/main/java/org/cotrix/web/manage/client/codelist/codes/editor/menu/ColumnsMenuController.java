/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.client.async.AsyncUtils.SuccessCallback;
import org.cotrix.web.common.shared.codelist.Definition;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.codes.event.GroupsSwitchedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.SwitchGroupsEvent;
import org.cotrix.web.manage.client.codelist.common.GroupFactory;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.ManagerBus;
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
	
	@Inject
	void bind(@ManagerBus EventBus eventBus) {
		eventBus.addHandler(DataSavedEvent.TYPE, new DataSavedEvent.DataSavedHandler() {

			@Override
			public void onDataSaved(final DataSavedEvent event) {
				DataEditEvent<?> editEvent = event.getEditEvent();
				
				if (editEvent.getData() instanceof Definition) {
					Definition definition = (Definition) editEvent.getData();
					switch (editEvent.getEditType()) {
						case ADD: {
							if (!activeDefinitionIds.contains(definition.getId())) {
								activeDefinitionIds.add(definition.getId());
								updateGroups();
							}
						} break;
						case UPDATE: updateGroups(); break;
						case REMOVE: {
							boolean removed = activeDefinitionIds.remove(definition.getId());
							if (removed) updateGroups();
						} break;
					}
				}
				
			}
		});
	}
	
	@EventHandler
	void onGroupsSwitched(GroupsSwitchedEvent event) {
		if (activeDefinitionIds == null) activeDefinitionIds = new ArrayList<String>();
		else activeDefinitionIds.clear();
		
		for (Group group:event.getGroups()) if (!activeDefinitionIds.contains(group.getDefinition().getId())) activeDefinitionIds.add(group.getDefinition().getId());
	}
	
	
	private void updateGroups() {
		List<Group> activeGroups = new ArrayList<Group>();
		Map<String, Group> groups = factory.getUniqueGroups();
		for (String activeId:activeDefinitionIds) activeGroups.add(groups.get(activeId));
		updateGroups(activeGroups);
	}
	
	private void updateGroups(List<Group> newActiveGroups) {
		Log.trace("updating groups");
		List<Group> groups = factory.expandGroups(newActiveGroups);
		bus.fireEvent(new SwitchGroupsEvent(groups));
	}	
}
