package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;

import com.google.gwt.user.client.ui.Widget;

public interface SummaryFormView<T> {
	
	public interface Presenter<T> {
		
	}
	void initForm(String[] headers);
	void setPresenter(SummaryFormPresenterImpl summaryFormPresenter);

	Widget asWidget();
}
