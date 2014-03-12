package org.cotrix.web.ingest.client.event;

import java.util.List;

import org.cotrix.web.ingest.shared.AttributeMapping;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingsUpdatedEvent extends GenericEvent {

	private List<AttributeMapping> mappings;
	public MappingsUpdatedEvent(List<AttributeMapping> mappings) {
		this.mappings = mappings;
	}

	public List<AttributeMapping> getMappings() {
		return mappings;
	}
}
