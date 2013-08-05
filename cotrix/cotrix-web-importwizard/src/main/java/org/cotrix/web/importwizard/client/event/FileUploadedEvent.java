package org.cotrix.web.importwizard.client.event;

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

	public FileUploadedEvent(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
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
