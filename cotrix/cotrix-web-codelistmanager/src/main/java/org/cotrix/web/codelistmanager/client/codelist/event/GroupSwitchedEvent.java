package org.cotrix.web.codelistmanager.client.codelist.event;

import org.cotrix.web.codelistmanager.shared.Group;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupSwitchedEvent extends
		GwtEvent<GroupSwitchedEvent.GroupSwitchedHandler> {

	public static Type<GroupSwitchedHandler> TYPE = new Type<GroupSwitchedHandler>();
	
	private Group group;
	protected GroupSwitchType switchType;

	public interface GroupSwitchedHandler extends EventHandler {
		void onGroupSwitched(GroupSwitchedEvent event);
	}

	public GroupSwitchedEvent(Group group, GroupSwitchType switchType) {
		this.group = group;
		this.switchType = switchType;
	}

	public Group getGroup() {
		return group;
	}	

	/**
	 * @return the switchType
	 */
	public GroupSwitchType getSwitchType() {
		return switchType;
	}

	@Override
	protected void dispatch(GroupSwitchedHandler handler) {
		handler.onGroupSwitched(this);
	}

	@Override
	public Type<GroupSwitchedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GroupSwitchedHandler> getType() {
		return TYPE;
	}
}
