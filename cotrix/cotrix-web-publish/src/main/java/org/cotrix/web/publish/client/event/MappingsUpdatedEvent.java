package org.cotrix.web.publish.client.event;

import org.cotrix.web.publish.shared.AttributesMappings;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingsUpdatedEvent extends GwtEvent<MappingsUpdatedEvent.MappingsUpdatedHandler> {

	public static Type<MappingsUpdatedHandler> TYPE = new Type<MappingsUpdatedHandler>();
	
	protected AttributesMappings mappings;

	public interface MappingsUpdatedHandler extends EventHandler {
		void onMappingUpdated(MappingsUpdatedEvent event);
	}

	public MappingsUpdatedEvent(AttributesMappings mappings) {
		this.mappings = mappings;
	}

	public AttributesMappings getMappings() {
		return mappings;
	}

	@Override
	protected void dispatch(MappingsUpdatedHandler handler) {
		handler.onMappingUpdated(this);
	}

	@Override
	public Type<MappingsUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingsUpdatedHandler> getType() {
		return TYPE;
	}

}
