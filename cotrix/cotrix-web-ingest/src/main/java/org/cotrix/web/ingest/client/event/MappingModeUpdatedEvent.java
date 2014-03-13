package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.MappingMode;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingModeUpdatedEvent extends GenericEvent {

	private MappingMode mappingMode;

	public MappingModeUpdatedEvent(MappingMode mappingMode) {
		this.mappingMode = mappingMode;
	}

	public MappingMode getMappingMode() {
		return mappingMode;
	}
}
