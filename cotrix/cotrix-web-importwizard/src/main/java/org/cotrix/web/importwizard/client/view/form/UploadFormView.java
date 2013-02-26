package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.vectomatic.file.FileList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface UploadFormView<T> {

	public interface Presenter<T> {
		void onLoadFileFinish();
		void onDeleteButtonClicked();
		void onBrowseButtonClicked();
		void onUploadFileChange(FileList fileList,String filename);
		void onError(String message);
	}
	void onDeleteButtonClicked(ClickEvent event);
	void onBrowseButtonClicked(ClickEvent event);
	void setFileUploadButtonClicked();
	void setPresenter(UploadFormPresenterImpl presenter);
	void setOnUploadFinish(String filename);
	void setOnDeleteButtonClicked();
	void onUploadFileChange(ChangeEvent event);
	Widget asWidget();
}
