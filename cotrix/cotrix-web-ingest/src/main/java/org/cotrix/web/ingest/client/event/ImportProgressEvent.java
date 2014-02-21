package org.cotrix.web.ingest.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.Progress;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgressEvent extends GwtEvent<ImportProgressEvent.ImportProgressHandler> {

	public static Type<ImportProgressHandler> TYPE = new Type<ImportProgressHandler>();
	private Progress progress;

	public interface ImportProgressHandler extends EventHandler {
		void onImportProgress(ImportProgressEvent event);
	}

	public ImportProgressEvent(Progress progress) {
		this.progress = progress;
	}

	public Progress getProgress() {
		return progress;
	}

	@Override
	protected void dispatch(ImportProgressHandler handler) {
		handler.onImportProgress(this);
	}

	@Override
	public Type<ImportProgressHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ImportProgressHandler> getType() {
		return TYPE;
	}
}
