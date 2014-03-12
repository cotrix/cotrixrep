package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.client.util.SourceType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceTypeChangeEvent extends GenericEvent {

	public static final SourceTypeChangeEvent CHANNEL = new SourceTypeChangeEvent(SourceType.CHANNEL);
	public static final SourceTypeChangeEvent FILE = new SourceTypeChangeEvent(SourceType.FILE);

	private SourceType sourceType;

	public SourceTypeChangeEvent(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public SourceType getSourceType() {
		return sourceType;
	}
}
