package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.codelist.attribute.Group;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupsChangedEvent extends GwtEvent<GroupsChangedEvent.GroupsChangedHandler> {

	public static Type<GroupsChangedHandler> TYPE = new Type<GroupsChangedHandler>();
	private Set<Group> groups;

	public interface GroupsChangedHandler extends EventHandler {
		void onGroupsChanged(GroupsChangedEvent event);
	}

	public interface HasGroupsChangedHandlers extends HasHandlers {
		HandlerRegistration addGroupsChangedHandler(GroupsChangedHandler handler);
	}

	public GroupsChangedEvent(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	@Override
	protected void dispatch(GroupsChangedHandler handler) {
		handler.onGroupsChanged(this);
	}

	@Override
	public Type<GroupsChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GroupsChangedHandler> getType() {
		return TYPE;
	}
}
