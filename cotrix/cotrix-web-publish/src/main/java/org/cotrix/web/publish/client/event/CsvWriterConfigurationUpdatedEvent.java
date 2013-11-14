package org.cotrix.web.publish.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.share.shared.CsvConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvWriterConfigurationUpdatedEvent	extends	GwtEvent<CsvWriterConfigurationUpdatedEvent.CsvWriterConfigurationUpdatedHandler> {

	public static Type<CsvWriterConfigurationUpdatedHandler> TYPE = new Type<CsvWriterConfigurationUpdatedHandler>();
	private CsvConfiguration configuration;

	public interface CsvWriterConfigurationUpdatedHandler extends EventHandler {
		void onCsvWriterConfigurationUpdated(
				CsvWriterConfigurationUpdatedEvent event);
	}

	public CsvWriterConfigurationUpdatedEvent(CsvConfiguration configuration) {
		this.configuration = configuration;
	}

	public CsvConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected void dispatch(CsvWriterConfigurationUpdatedHandler handler) {
		handler.onCsvWriterConfigurationUpdated(this);
	}

	@Override
	public Type<CsvWriterConfigurationUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CsvWriterConfigurationUpdatedHandler> getType() {
		return TYPE;
	}
}
