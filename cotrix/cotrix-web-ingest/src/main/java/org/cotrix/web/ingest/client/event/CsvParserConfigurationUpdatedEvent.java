package org.cotrix.web.ingest.client.event;

import org.cotrix.web.common.shared.CsvConfiguration;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationUpdatedEvent	extends	GenericEvent {
	private CsvConfiguration configuration;

	public CsvParserConfigurationUpdatedEvent(CsvConfiguration configuration) {
		this.configuration = configuration;
	}

	public CsvConfiguration getConfiguration() {
		return configuration;
	}
}
