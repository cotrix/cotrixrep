package org.cotrix.web.publish.client.event;

import org.cotrix.web.share.shared.Progress.Status;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishCompleteEvent extends GwtEvent<PublishCompleteEvent.PublishCompleteHandler> {

	public static Type<PublishCompleteHandler> TYPE = new Type<PublishCompleteHandler>();
	private Status status;

	public interface PublishCompleteHandler extends EventHandler {
		void onPublishComplete(PublishCompleteEvent event);
	}

	public PublishCompleteEvent(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	protected void dispatch(PublishCompleteHandler handler) {
		handler.onPublishComplete(this);
	}

	@Override
	public Type<PublishCompleteHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PublishCompleteHandler> getType() {
		return TYPE;
	}
}
