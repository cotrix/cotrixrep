package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.ImportProgress;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgressEvent extends GenericEvent {

	private ImportProgress progress;

	public ImportProgressEvent(ImportProgress progress) {
		this.progress = progress;
	}

	public ImportProgress getProgress() {
		return progress;
	}
}
