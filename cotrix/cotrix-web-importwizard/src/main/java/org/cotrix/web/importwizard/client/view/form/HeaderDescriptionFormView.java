package org.cotrix.web.importwizard.client.view.form;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenterImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormPresenterImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderDescriptionFormView<T> {
	public interface Presenter<T> {
		
	}
	void initForm(String[] headers);
	void alert(String message);
	void setPresenter(HeaderDescriptionPresenterImpl presenter);
	HashMap<String, String> getHeaderDescription();
	Widget asWidget();
	
}
