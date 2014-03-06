package org.cotrix.web.ingest.client.event;

import java.util.List;

import com.google.web.bindery.event.shared.binder.GenericEvent;

import org.cotrix.web.ingest.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadedEvent extends GenericEvent {

	private List<AttributeMapping> mappings;

	public MappingLoadedEvent(List<AttributeMapping> mappings) {
		this.mappings = mappings;
	}

	public List<AttributeMapping> getMappings() {
		return mappings;
	}
}
