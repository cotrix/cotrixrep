package org.cotrix.web.share.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class StatusUpdatedEvent extends GwtEvent<StatusUpdatedEvent.StatusUpdatedHandler> {

	public static Type<StatusUpdatedHandler> TYPE = new Type<StatusUpdatedHandler>();
	private String status;

	public interface StatusUpdatedHandler extends EventHandler {
		void onStatusUpdated(StatusUpdatedEvent event);
	}

	public StatusUpdatedEvent(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	@Override
	protected void dispatch(StatusUpdatedHandler handler) {
		handler.onStatusUpdated(this);
	}

	@Override
	public Type<StatusUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<StatusUpdatedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String status) {
		source.fireEvent(new StatusUpdatedEvent(status));
	}
}
