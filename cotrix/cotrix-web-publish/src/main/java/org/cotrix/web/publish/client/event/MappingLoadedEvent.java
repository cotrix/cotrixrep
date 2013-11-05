package org.cotrix.web.publish.client.event;

import java.util.List;

import org.cotrix.web.publish.shared.AttributeMapping;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadedEvent extends GwtEvent<MappingLoadedEvent.MappingLoadedHandler> {

	public static Type<MappingLoadedHandler> TYPE = new Type<MappingLoadedHandler>();
	private List<AttributeMapping> mappings;

	public interface MappingLoadedHandler extends EventHandler {
		void onMappingLoaded(MappingLoadedEvent event);
	}

	public MappingLoadedEvent(List<AttributeMapping> mappings) {
		this.mappings = mappings;
	}

	public List<AttributeMapping> getMappings() {
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