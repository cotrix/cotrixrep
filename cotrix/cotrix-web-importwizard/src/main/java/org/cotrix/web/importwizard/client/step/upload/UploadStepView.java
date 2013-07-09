package org.cotrix.web.importwizard.client.step.upload;

import org.vectomatic.file.FileList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface UploadStepView {

	public interface Presenter {
		void onLoadFileFinish();
		void onDeleteButtonClicked();
		void onBrowseButtonClicked();
		void onUploadFileChange(FileList fileList, String filename);
		void onError(String message);
		void onSubmitComplete(SubmitCompleteEvent event);
	}
	
	void setPresenter(Presenter presenter);
	void onDeleteButtonClicked(ClickEvent event);
	void onBrowseButtonClicked(ClickEvent event);
	void setFileUploadButtonClicked();
	void setOnUploadFinish(String filename);
	void setOnDeleteButtonClicked();
	void onUploadFileChange(ChangeEvent event);
	void setCotrixModelFieldValue(String modelInJSON);
	void alert(String message);
	void reset();
	void submitForm();
	Widget asWidget();
}
