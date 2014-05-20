package org.cotrix.web.ingest.client.event;

import java.util.List;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvHeadersEvent extends GenericEvent {
	
	private List<String> headers;

	public CsvHeadersEvent(List<String> headers) {
		this.headers = headers;
	}

	public List<String> getHeaders() {
		return headers;
	}

}
