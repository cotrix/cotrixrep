package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeSwitchedEvent extends
		GwtEvent<AttributeSwitchedEvent.AttributeSwitchedHandler> {

	public static Type<AttributeSwitchedHandler> TYPE = new Type<AttributeSwitchedHandler>();
	
	private String attributeName;
	protected AttributeSwitchType switchType;

	public interface AttributeSwitchedHandler extends EventHandler {
		void onAttributeSwitched(AttributeSwitchedEvent event);
	}

	public AttributeSwitchedEvent(String attributeName, AttributeSwitchType switchType) {
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
	protected void dispatch(AttributeSwitchedHandler handler) {
		handler.onAttributeSwitched(this);
	}

	@Override
	public Type<AttributeSwitchedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AttributeSwitchedHandler> getType() {
		return TYPE;
	}
}
