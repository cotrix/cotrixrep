package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SaveEvent extends GwtEvent<SaveEvent.SaveHandler> {

	public static Type<SaveHandler> TYPE = new Type<SaveHandler>();

	public interface SaveHandler extends EventHandler {
		void onSave(SaveEvent event);
	}

	public SaveEvent() {
	}

	@Override
	protected void dispatch(SaveHandler handler) {
		handler.onSave(this);
	}

	@Override
	public Type<SaveHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SaveHandler> getType() {
		return TYPE;
	}
}
