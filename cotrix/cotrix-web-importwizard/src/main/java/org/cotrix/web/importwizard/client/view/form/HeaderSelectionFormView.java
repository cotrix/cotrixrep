package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderSelectionFormView<T> {
	public interface Presenter<T> {

	}
	void onChecked(ClickEvent event);
	void setPresenter(HeaderSelectionFormPresenterImpl presenter);
	Widget asWidget();
}
