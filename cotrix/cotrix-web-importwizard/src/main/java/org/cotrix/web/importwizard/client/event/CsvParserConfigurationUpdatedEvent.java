package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationUpdatedEvent	extends	GwtEvent<CsvParserConfigurationUpdatedEvent.CsvParserConfigurationUpdatedHandler> {

	public static Type<CsvParserConfigurationUpdatedHandler> TYPE = new Type<CsvParserConfigurationUpdatedHandler>();
	private CsvParserConfiguration configuration;

	public interface CsvParserConfigurationUpdatedHandler extends EventHandler {
		void onCsvParserConfigurationUpdated(
				CsvParserConfigurationUpdatedEvent event);
	}

	public CsvParserConfigurationUpdatedEvent(CsvParserConfiguration configuration) {
		this.configuration = configuration;
	}

	public CsvParserConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected void dispatch(CsvParserConfigurationUpdatedHandler handler) {
		handler.onCsvParserConfigurationUpdated(this);
	}

	@Override
	public Type<CsvParserConfigurationUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CsvParserConfigurationUpdatedHandler> getType() {
		return TYPE;
	}
}
