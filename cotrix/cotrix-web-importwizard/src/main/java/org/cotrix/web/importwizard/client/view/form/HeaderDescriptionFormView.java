package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderDescriptionFormView<T> {
	public interface Presenter<T> {
		
	}
	void initForm(String[] headers);
	void setPresenter(HeaderDescriptionPresenterImpl presenter);

	Widget asWidget();

}
