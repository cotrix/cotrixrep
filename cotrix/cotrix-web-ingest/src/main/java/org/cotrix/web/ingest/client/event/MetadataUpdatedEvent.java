package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.ImportMetadata;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataUpdatedEvent extends GenericEvent {

	private ImportMetadata metadata;
	private boolean userEdited;

	public MetadataUpdatedEvent(ImportMetadata metadata, boolean userEdited) {
		this.metadata = metadata;
		this.userEdited = userEdited;
	}

	public ImportMetadata getMetadata() {
		return metadata;
	}

	/**
	 * @return the userEdited
	 */
	public boolean isUserEdited() {
		return userEdited;
	}
}
