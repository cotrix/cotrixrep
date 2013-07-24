package org.cotrix.web.importwizard.client.step.upload;

import org.vectomatic.file.FileList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface UploadStepView {

	public interface Presenter {
		void onDeleteButtonClicked();
		void onRetryButtonClicked();
		void onUploadFileChanged(FileList fileList, String filename);
		void onError(String message);
		void onSubmitComplete(SubmitCompleteEvent event);
	}
	
	void setPresenter(Presenter presenter);
	public void setupUpload(String filename, long filesize);
	public void setUploadProgress(int progress);
	public void setUploadFailed();
	public void setUploadComplete(String codeListType);

	void resetFileUpload();
	void onUploadFileChange(ChangeEvent event);

	void alert(String message);
	void reset();
	void submitForm();
	Widget asWidget();
}
