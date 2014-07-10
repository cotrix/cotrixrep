package org.cotrix.web.manage.client.codelist.codes.event;

import org.cotrix.web.manage.shared.Group;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupSwitchedEvent extends GenericEvent {
	
	private Group group;
	protected GroupSwitchType switchType;

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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupSwitchedEvent [group=");
		builder.append(group);
		builder.append(", switchType=");
		builder.append(switchType);
		builder.append("]");
		return builder.toString();
	}
}