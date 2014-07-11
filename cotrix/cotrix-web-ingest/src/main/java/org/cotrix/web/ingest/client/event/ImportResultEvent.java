package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.ImportResult;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportResultEvent extends GenericEvent {

	private ImportResult result;

	public ImportResultEvent(ImportResult result) {
		this.result = result;
	}

	public ImportResult getResult() {
		return result;
	}
}
