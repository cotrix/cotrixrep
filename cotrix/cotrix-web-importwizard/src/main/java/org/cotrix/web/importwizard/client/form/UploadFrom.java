package org.cotrix.web.importwizard.client.form;

import org.apache.tools.ant.taskdefs.Javadoc.Html;
import org.vectomatic.file.ErrorCode;
import org.vectomatic.file.File;
import org.vectomatic.file.FileError;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;
import org.vectomatic.file.events.LoadStartEvent;
import org.vectomatic.file.events.LoadStartHandler;
import org.vectomatic.file.events.ProgressEvent;
import org.vectomatic.file.events.ProgressHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UploadFrom extends Composite {

	private static UploadFromUiBinder uiBinder = GWT
			.create(UploadFromUiBinder.class);

	interface UploadFromUiBinder extends UiBinder<Widget, UploadFrom> {
	}

	@UiField
	FileUploadExt fileUpload;
	
	@UiField
	Label fileNameLabel;
	
	@UiField
	HTML closeButton;
	@UiHandler("closeButton")
	public void close(ClickEvent event) {
		
		fileNameLabel.setText("");
		closeButton.setVisible(false);
		filename = "";
	}
	
	
	@UiField
	Button browseButton;
	@UiHandler("browseButton")
	public void browse(ClickEvent event) {
		fileUpload.click();
	}
	
	private FileReader reader;
	private String filename;
	
	public UploadFrom() {
		initWidget(uiBinder.createAndBindUi(this));
		initFileReader();
		
	}
	private void onLoadFileFinish(){
		fileNameLabel.setText(filename);
		closeButton.setVisible(true);
	}
	private void initFileReader() {

		// Create a file reader a and queue of files to read.
				// UI event handler will populate this queue by calling queueFiles()
				reader = new FileReader();
				reader.addLoadEndHandler(new LoadEndHandler() {
					/**
					 * This handler is invoked when FileReader.readAsText(),
					 * FileReader.readAsBinaryString() or FileReader.readAsArrayBuffer()
					 * successfully completes
					 */
					public void onLoadEnd(LoadEndEvent event) {
						onLoadFileFinish();
						System.out.println("---->end");
					}
					
				});
				
				reader.addLoadStartHandler(new LoadStartHandler() {
					public void onLoadStart(LoadStartEvent event) {
						System.out.println("---->start");
						
					}
				});
				reader.addProgressHandler(new ProgressHandler() {
					
					public void onProgress(ProgressEvent event) {
						 System.out.println("---->progress"+event.loaded());
					}
				});
				reader.addErrorHandler(new ErrorHandler() {
					/**
					 * This handler is invoked when FileReader.readAsText(),
					 * FileReader.readAsBinaryString() or FileReader.readAsArrayBuffer()
					 * fails
					 */
					public void onError(ErrorEvent event) {
						Window.alert(event.toString());
					}
				});
	}

	@UiHandler("fileUpload")
	public void onUploadFile(ChangeEvent event) {
		processFiles(fileUpload.getFiles());
	}

	private void processFiles(FileList files) {
		if(files.getLength() == 0) return;

		File file = files.getItem(0);
		String type = file.getType();
		try {
			if ("image/svg+xml".equals(type)) {
				Window.alert("Only CSV or Text file.");
			} else if (type.startsWith("image/png")) {
				Window.alert("Only CSV or Text file.");
			} else if (type.startsWith("image/")) {
				Window.alert("Only CSV or Text file.");
			} else if (type.startsWith("text/")) {
				reader.readAsText(file);	
				filename = file.getName();
			}
		} catch(Throwable t) {
			// Necessary for FF (see bug https://bugzilla.mozilla.org/show_bug.cgi?id=701154)
			// Standard-complying browsers will not go in this branch
			handleError(file);
		}

	}
	private void handleError(File file) {
		FileError error = reader.getError();
		String errorDesc = "";
		if (error != null) {
			ErrorCode errorCode = error.getCode();
			if (errorCode != null) {
				errorDesc = ": " + errorCode.name();
			}
		}
		Window.alert("File loading error for file: " + file.getName() + "\n" + errorDesc);
	}


}
