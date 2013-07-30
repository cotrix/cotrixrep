package org.cotrix.web.importwizard.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.importwizard.shared.CsvPreviewData;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewDataUpdatedEvent extends GwtEvent<PreviewDataUpdatedEvent.PreviewDataUpdatedHandler> {

	public static Type<PreviewDataUpdatedHandler> TYPE = new Type<PreviewDataUpdatedHandler>();
	private CsvPreviewData previewData;

	public interface PreviewDataUpdatedHandler extends EventHandler {
		void onPreviewDataUpdated(PreviewDataUpdatedEvent event);
	}

	public PreviewDataUpdatedEvent(CsvPreviewData previewData) {
		this.previewData = previewData;
	}

	public CsvPreviewData getPreviewData() {
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
