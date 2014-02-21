package org.cotrix.web.ingest.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportStartedEvent extends	GwtEvent<ImportStartedEvent.ImportStartedHandler> {

	public static Type<ImportStartedHandler> TYPE = new Type<ImportStartedHandler>();

	public interface ImportStartedHandler extends EventHandler {
		void onImportStarted(ImportStartedEvent event);
	}

	public ImportStartedEvent() {
	}

	@Override
	protected void dispatch(ImportStartedHandler handler) {
		handler.onImportStarted(this);
	}

	@Override
	public Type<ImportStartedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ImportStartedHandler> getType() {
		return TYPE;
	}
}
