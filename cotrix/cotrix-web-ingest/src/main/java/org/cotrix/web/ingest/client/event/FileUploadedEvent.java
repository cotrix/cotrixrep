package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.UIAssetType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadedEvent extends GenericEvent {

	private String fileName;
	private UIAssetType assetType;

	public FileUploadedEvent(String fileName, UIAssetType assetType) {
		this.fileName = fileName;
		this.assetType = assetType;
	}

	public String getFileName() {
		return fileName;
	}

	public UIAssetType getAssetType() {
		return assetType;
	}
}
