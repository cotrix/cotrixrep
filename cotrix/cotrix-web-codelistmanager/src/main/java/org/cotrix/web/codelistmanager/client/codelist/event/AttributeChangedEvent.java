package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;

import org.cotrix.web.share.shared.UIAttribute;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeChangedEvent extends GwtEvent<AttributeChangedEvent.AttributeChangedHandler> {

	public static Type<AttributeChangedHandler> TYPE = new Type<AttributeChangedHandler>();
	
	private String oldName;
	private UIAttribute attribute;

	public interface AttributeChangedHandler extends EventHandler {
		void onAttributeChanged(AttributeChangedEvent event);
	}

	public interface HasAttributeChangedHandlers extends HasHandlers {
		HandlerRegistration addAttributeChangedHandler(
				AttributeChangedHandler handler);
	}

	public AttributeChangedEvent(String oldName, UIAttribute attribute) {
		this.oldName = oldName;
		this.attribute = attribute;
	}

	public String getOldName() {
		return oldName;
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

	public static void fire(HasHandlers source, String oldName, UIAttribute attribute) {
		source.fireEvent(new AttributeChangedEvent(oldName, attribute));
	}
}
