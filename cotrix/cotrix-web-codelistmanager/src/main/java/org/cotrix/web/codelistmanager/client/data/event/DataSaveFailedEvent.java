package org.cotrix.web.codelistmanager.client.data.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataSaveFailedEvent extends GwtEvent<DataSaveFailedEvent.DataSaveFailedHandler> {

	public static Type<DataSaveFailedHandler> TYPE = new Type<DataSaveFailedHandler>();
	private Throwable cause;

	public interface DataSaveFailedHandler extends EventHandler {
		void onDataSaveFailed(DataSaveFailedEvent event);
	}

	public interface HasDataSaveFailedHandlers extends HasHandlers {
		HandlerRegistration addDataSaveFailedHandler(DataSaveFailedHandler handler);
	}

	public DataSaveFailedEvent(Throwable cause) {
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	@Override
	protected void dispatch(DataSaveFailedHandler handler) {
		handler.onDataSaveFailed(this);
	}

	@Override
	public Type<DataSaveFailedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DataSaveFailedHandler> getType() {
		return TYPE;
	}
}
