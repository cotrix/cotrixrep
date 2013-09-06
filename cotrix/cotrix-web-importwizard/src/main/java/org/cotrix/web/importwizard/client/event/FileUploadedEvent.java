package org.cotrix.web.importwizard.client.event;

import org.cotrix.web.importwizard.shared.CodeListType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadedEvent extends GwtEvent<FileUploadedEvent.FileUploadedHandler> {

	public static Type<FileUploadedHandler> TYPE = new Type<FileUploadedHandler>();

	public interface FileUploadedHandler extends EventHandler {
		void onFileUploaded(FileUploadedEvent event);
	}
	
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

	@Override
	protected void dispatch(FileUploadedHandler handler) {
		handler.onFileUploaded(this);
	}

	@Override
	public Type<FileUploadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FileUploadedHandler> getType() {
		return TYPE;
	}
}
