package org.cotrix.web.publish.client.event;

import org.cotrix.web.publish.shared.ImportProgress;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishProgressEvent extends GwtEvent<PublishProgressEvent.PublishProgressHandler> {

	public static Type<PublishProgressHandler> TYPE = new Type<PublishProgressHandler>();
	private ImportProgress progress;

	public interface PublishProgressHandler extends EventHandler {
		void onPublishProgress(PublishProgressEvent event);
	}

	public PublishProgressEvent(ImportProgress progress) {
		this.progress = progress;
	}

	public ImportProgress getProgress() {
		return progress;
	}

	@Override
	protected void dispatch(PublishProgressHandler handler) {
		handler.onPublishProgress(this);
	}

	@Override
	public Type<PublishProgressHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PublishProgressHandler> getType() {
		return TYPE;
	}
}
