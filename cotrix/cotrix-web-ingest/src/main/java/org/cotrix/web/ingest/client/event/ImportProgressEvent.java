package org.cotrix.web.ingest.client.event;

import org.cotrix.web.common.shared.Progress;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgressEvent extends GenericEvent {

	private Progress progress;

	public ImportProgressEvent(Progress progress) {
		this.progress = progress;
	}

	public Progress getProgress() {
		return progress;
	}
}
