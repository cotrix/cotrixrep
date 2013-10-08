package org.cotrix.web.codelistmanager.client.codelist.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import java.util.Set;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeSetChangedEvent extends GwtEvent<AttributeSetChangedEvent.AttributeSetChangedHandler> {

	public static Type<AttributeSetChangedHandler> TYPE = new Type<AttributeSetChangedHandler>();
	private Set<String> attributesNames;

	public interface AttributeSetChangedHandler extends EventHandler {
		void onAttributeSetChanged(AttributeSetChangedEvent event);
	}

	public interface HasAttributeSetChangedHandlers extends HasHandlers {
		HandlerRegistration addAttributeSetChangedHandler(AttributeSetChangedHandler handler);
	}

	public AttributeSetChangedEvent(Set<String> attributesNames) {
		this.attributesNames = attributesNames;
	}

	public Set<String> getAttributesNames() {
		return attributesNames;
	}

	@Override
	protected void dispatch(AttributeSetChangedHandler handler) {
		handler.onAttributeSetChanged(this);
	}

	@Override
	public Type<AttributeSetChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AttributeSetChangedHandler> getType() {
		return TYPE;
	}
}
