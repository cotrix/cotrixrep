package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.share.shared.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchAttributeEvent extends
		GwtEvent<SwitchAttributeEvent.SwitchAttributeHandler> {

	public static Type<SwitchAttributeHandler> TYPE = new Type<SwitchAttributeHandler>();
	
	private UIAttribute attribute;
	protected AttributeSwitchType switchType;

	public interface SwitchAttributeHandler extends EventHandler {
		void onSwitchAttribute(SwitchAttributeEvent event);
	}

	public SwitchAttributeEvent(UIAttribute attribute, AttributeSwitchType switchType) {
		this.attribute = attribute;
		this.switchType = switchType;
	}

	public UIAttribute getAttribute() {
		return attribute;
	}	

	/**
	 * @return the switchType
	 */
	public AttributeSwitchType getSwitchType() {
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
