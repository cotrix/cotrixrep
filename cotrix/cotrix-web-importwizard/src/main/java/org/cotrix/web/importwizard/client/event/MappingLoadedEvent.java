package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.AttributesMappings;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadedEvent extends GwtEvent<MappingLoadedEvent.MappingLoadedHandler> {

	public static Type<MappingLoadedHandler> TYPE = new Type<MappingLoadedHandler>();
	private AttributesMappings mappings;

	public interface MappingLoadedHandler extends EventHandler {
		void onMappingLoaded(MappingLoadedEvent event);
	}

	public MappingLoadedEvent(AttributesMappings mappings) {
		this.mappings = mappings;
	}

	public AttributesMappings getMappings() {
		return mappings;
	}

	@Override
	protected void dispatch(MappingLoadedHandler handler) {
		handler.onMappingLoaded(this);
	}

	@Override
	public Type<MappingLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MappingLoadedHandler> getType() {
		return TYPE;
	}
}
