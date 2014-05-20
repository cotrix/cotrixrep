package org.cotrix.web.manage.client.data.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataSavedEvent extends GwtEvent<DataSavedEvent.DataSavedHandler> {

	public static Type<DataSavedHandler> TYPE = new Type<DataSavedHandler>();

	public interface DataSavedHandler extends EventHandler {
		void onDataSaved(DataSavedEvent event);
	}

	public interface HasDataSavedHandlers extends HasHandlers {
		HandlerRegistration addDataSavedHandler(DataSavedHandler handler);
	}

	public DataSavedEvent() {
	}

	@Override
	protected void dispatch(DataSavedHandler handler) {
		handler.onDataSaved(this);
	}

	@Override
	public Type<DataSavedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DataSavedHandler> getType() {
		return TYPE;
	}
}
