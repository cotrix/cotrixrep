package org.cotrix.web.codelistmanager.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.share.shared.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeSwitchedEvent extends
		GwtEvent<AttributeSwitchedEvent.AttributeSwitchedHandler> {

	public static Type<AttributeSwitchedHandler> TYPE = new Type<AttributeSwitchedHandler>();
	
	private UIAttribute attribute;
	protected AttributeSwitchType switchType;

	public interface AttributeSwitchedHandler extends EventHandler {
		void onAttributeSwitched(AttributeSwitchedEvent event);
	}

	public AttributeSwitchedEvent(UIAttribute attribute, AttributeSwitchType switchType) {
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
