package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchAttributeEvent extends
		GwtEvent<SwitchAttributeEvent.SwitchAttributeHandler> {

	public static Type<SwitchAttributeHandler> TYPE = new Type<SwitchAttributeHandler>();
	
	protected String attributeName;
	protected AttributeSwitchType switchType;

	public interface SwitchAttributeHandler extends EventHandler {
		void onSwitchAttribute(SwitchAttributeEvent event);
	}

	public SwitchAttributeEvent(String attributeName, AttributeSwitchType switchType) {
		this.attributeName = attributeName;
		this.switchType = switchType;
	}

	public String getAttributeName() {
		return attributeName;
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
