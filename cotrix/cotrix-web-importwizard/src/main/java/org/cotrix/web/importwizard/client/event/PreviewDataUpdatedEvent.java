package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.CodeListPreviewData;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewDataUpdatedEvent extends GwtEvent<PreviewDataUpdatedEvent.PreviewDataUpdatedHandler> {

	public static Type<PreviewDataUpdatedHandler> TYPE = new Type<PreviewDataUpdatedHandler>();
	private CodeListPreviewData previewData;

	public interface PreviewDataUpdatedHandler extends EventHandler {
		void onPreviewDataUpdated(PreviewDataUpdatedEvent event);
	}

	public PreviewDataUpdatedEvent(CodeListPreviewData previewData) {
		this.previewData = previewData;
	}

	public CodeListPreviewData getPreviewData() {
		return previewData;
	}

	@Override
	protected void dispatch(PreviewDataUpdatedHandler handler) {
		handler.onPreviewDataUpdated(this);
	}

	@Override
	public Type<PreviewDataUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PreviewDataUpdatedHandler> getType() {
		return TYPE;
	}
}
