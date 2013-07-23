package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class StartImportEvent extends GwtEvent<StartImportEvent.StartImportHandler> {

	public static Type<StartImportHandler> TYPE = new Type<StartImportHandler>();

	public interface StartImportHandler extends EventHandler {
		void onStartImport(StartImportEvent event);
	}

	public StartImportEvent() {
	}

	@Override
	protected void dispatch(StartImportHandler handler) {
		handler.onStartImport(this);
	}

	@Override
	public Type<StartImportHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<StartImportHandler> getType() {
		return TYPE;
	}
}
