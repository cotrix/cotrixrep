package org.cotrix.web.manage.client.data.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SavingDataEvent extends GwtEvent<SavingDataEvent.SavingDataHandler> {

	public static Type<SavingDataHandler> TYPE = new Type<SavingDataHandler>();

	public interface SavingDataHandler extends EventHandler {
		void onSavingData(SavingDataEvent event);
	}

	public interface HasSavingDataHandlers extends HasHandlers {
		HandlerRegistration addSavingDataHandler(SavingDataHandler handler);
	}

	public SavingDataEvent() {
	}

	@Override
	protected void dispatch(SavingDataHandler handler) {
		handler.onSavingData(this);
	}

	@Override
	public Type<SavingDataHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SavingDataHandler> getType() {
		return TYPE;
	}
}
