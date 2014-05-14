package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.UIAssetType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadedEvent extends GenericEvent {

	protected String fileName;
	protected UIAssetType codeListType;

	public FileUploadedEvent(String fileName, UIAssetType codeListType) {
		this.fileName = fileName;
		this.codeListType = codeListType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the codeListType
	 */
	public UIAssetType getCodeListType() {
		return codeListType;
	}
}
