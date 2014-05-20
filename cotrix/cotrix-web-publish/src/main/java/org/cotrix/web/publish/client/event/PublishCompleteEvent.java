package org.cotrix.web.publish.client.event;

import org.cotrix.web.common.shared.Progress;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishCompleteEvent extends GwtEvent<PublishCompleteEvent.PublishCompleteHandler> {

	public static Type<PublishCompleteHandler> TYPE = new Type<PublishCompleteHandler>();
	
	private Progress progress;
	private String downloadUrl;

	public interface PublishCompleteHandler extends EventHandler {
		void onPublishComplete(PublishCompleteEvent event);
	}

	public PublishCompleteEvent(Progress progress, String downloadUrl) {
		this.progress = progress;
		this.downloadUrl = downloadUrl;
	}

	public Progress getProgress() {
		return progress;
	}

	public String getDownloadUrl() {
		return downloadUrl;
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
