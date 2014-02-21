package org.cotrix.web.manage.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.codelist.UIAttribute;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeChangedEvent extends GwtEvent<AttributeChangedEvent.AttributeChangedHandler> {

	public static Type<AttributeChangedHandler> TYPE = new Type<AttributeChangedHandler>();
	
	private UIAttribute attribute;

	public interface AttributeChangedHandler extends EventHandler {
		void onAttributeChanged(AttributeChangedEvent event);
	}

	public interface HasAttributeChangedHandlers extends HasHandlers {
		HandlerRegistration addAttributeChangedHandler(
				AttributeChangedHandler handler);
	}

	public AttributeChangedEvent(UIAttribute attribute) {
		this.attribute = attribute;
	}

	public UIAttribute getAttribute() {
		return attribute;
	}

	@Override
	protected void dispatch(AttributeChangedHandler handler) {
		handler.onAttributeChanged(this);
	}

	@Override
	public Type<AttributeChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AttributeChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UIAttribute attribute) {
		source.fireEvent(new AttributeChangedEvent(attribute));
	}
}
