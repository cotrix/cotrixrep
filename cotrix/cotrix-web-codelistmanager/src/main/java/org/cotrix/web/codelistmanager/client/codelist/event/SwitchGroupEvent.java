package org.cotrix.web.codelistmanager.client.codelist.event;

import org.cotrix.web.codelistmanager.shared.Group;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchGroupEvent extends
		GwtEvent<SwitchGroupEvent.SwitchAttributeHandler> {

	public static Type<SwitchAttributeHandler> TYPE = new Type<SwitchAttributeHandler>();
	
	protected Group group;
	protected GroupSwitchType switchType;

	public interface SwitchAttributeHandler extends EventHandler {
		void onSwitchAttribute(SwitchGroupEvent event);
	}

	public SwitchGroupEvent(Group group, GroupSwitchType switchType) {
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
	protected void dispatch(SwitchAttributeHandler handler) {
		handler.onSwitchAttribute(this);
	}

	@Override
	public Type<SwitchAttributeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SwitchAttributeHandler> getType() {
		return TYPE;
	}
}
