package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface UploadFormView<T> {
	void setPresenter(UploadFormPresenterImpl uploadFormPresenter);

	public interface Presenter<T> {
		void onLoadFileFinish(String filename);
		void onDeleteButtonClicked();
		void onBrowseButtonClicked();
		void onError(String message);
	}
	void onDeleteButtonClicked(ClickEvent event);
	void onBrowseButtonClicked(ClickEvent event);
	Widget asWidget();
}
