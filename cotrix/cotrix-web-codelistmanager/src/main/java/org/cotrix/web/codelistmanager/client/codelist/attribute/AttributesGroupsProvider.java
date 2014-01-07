/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist.attribute;

import java.util.Set;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.GroupsChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.HasGroupsChangedHandlers;
import org.cotrix.web.codelistmanager.shared.Group;
import org.cotrix.web.share.client.error.ManagedFailureCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesGroupsProvider implements HasGroupsChangedHandlers {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	
	@Inject
	protected ManagerServiceAsync managerService;
	
	@Inject @CodelistId
	protected String codelistId;
	
	public void loadGroups() {
		Log.trace("loading attributes groups for "+codelistId);
		managerService.getAttributesGroups(codelistId, new ManagedFailureCallback<Set<Group>>() {

			@Override
			public void onSuccess(Set<Group> groups) {
				handlerManager.fireEvent(new GroupsChangedEvent(groups));
			}
		});
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addGroupsChangedHandler(GroupsChangedHandler handler) {
		return handlerManager.addHandler(GroupsChangedEvent.TYPE, handler);
	}

}
