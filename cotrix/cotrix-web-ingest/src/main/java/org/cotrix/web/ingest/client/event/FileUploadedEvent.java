package org.cotrix.web.ingest.client.event;

import org.cotrix.web.ingest.shared.CodeListType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadedEvent extends GenericEvent {

	protected String fileName;
	protected CodeListType codeListType;

	public FileUploadedEvent(String fileName, CodeListType codeListType) {
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
	public CodeListType getCodeListType() {
		return codeListType;
	}
}
