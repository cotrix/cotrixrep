package org.cotrix.web.publish.client.event;

import org.cotrix.web.publish.shared.ImportMetadata;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataUpdatedEvent extends GwtEvent<MetadataUpdatedEvent.MetadataUpdatedHandler> {

	public static Type<MetadataUpdatedHandler> TYPE = new Type<MetadataUpdatedHandler>();
	

	protected ImportMetadata metadata;
	protected boolean userEdited;

	public interface MetadataUpdatedHandler extends EventHandler {
		void onMetadataUpdated(MetadataUpdatedEvent event);
	}

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

	@Override
	protected void dispatch(MetadataUpdatedHandler handler) {
		handler.onMetadataUpdated(this);
	}

	@Override
	public Type<MetadataUpdatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MetadataUpdatedHandler> getType() {
		return TYPE;
	}
}
