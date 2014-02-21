package org.cotrix.web.importwizard.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingsUpdatedEvent extends GwtEvent<MappingsUpdatedEvent.MappingsUpdatedHandler> {

	public static Type<MappingsUpdatedHandler> TYPE = new Type<MappingsUpdatedHandler>();
	private List<AttributeMapping> mappings;

	public interface MappingsUpdatedHandler extends EventHandler {
		void onMappingUpdated(MappingsUpdatedEvent event);
	}

	public MappingsUpdatedEvent(List<AttributeMapping> mappings) {
		this.mappings = mappings;
	}

	public List<AttributeMapping> getMappings() {
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
