package org.cotrix.web.ingest.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingLoadFailedEvent extends	GenericEvent {

	private Throwable throwable;
	
	public MappingLoadFailedEvent(Throwable throwable) {
		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return throwable;
	}
}
