package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationEditedEvent extends GwtEvent<CsvParserConfigurationEditedEvent.CsvParserConfigurationEditedHandler> {

	public static Type<CsvParserConfigurationEditedHandler> TYPE = new Type<CsvParserConfigurationEditedHandler>();
	private CsvParserConfiguration configuration;

	public interface CsvParserConfigurationEditedHandler extends EventHandler {
		void onCsvParserConfigurationEdited(CsvParserConfigurationEditedEvent event);
	}

	public CsvParserConfigurationEditedEvent(CsvParserConfiguration configuration) {
		this.configuration = configuration;
	}

	public CsvParserConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	protected void dispatch(CsvParserConfigurationEditedHandler handler) {
		handler.onCsvParserConfigurationEdited(this);
	}

	@Override
	public Type<CsvParserConfigurationEditedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CsvParserConfigurationEditedHandler> getType() {
		return TYPE;
	}
}
