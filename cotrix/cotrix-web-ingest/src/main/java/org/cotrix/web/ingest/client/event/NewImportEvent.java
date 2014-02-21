package org.cotrix.web.ingest.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewImportEvent extends GwtEvent<NewImportEvent.NewImportHandler> {

	public static Type<NewImportHandler> TYPE = new Type<NewImportHandler>();

	public interface NewImportHandler extends EventHandler {
		void onNewImport(NewImportEvent event);
	}

	public NewImportEvent() {
	}

	@Override
	protected void dispatch(NewImportHandler handler) {
		handler.onNewImport(this);
	}

	@Override
	public Type<NewImportHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewImportHandler> getType() {
		return TYPE;
	}
}
