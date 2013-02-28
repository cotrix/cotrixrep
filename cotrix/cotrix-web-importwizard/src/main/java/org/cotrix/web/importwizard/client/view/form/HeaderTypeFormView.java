package org.cotrix.web.importwizard.client.view.form;

import java.util.HashMap;

import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.shared.HeaderType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface HeaderTypeFormView<T> {
	public interface Presenter<T> {

	}
	HashMap<String, HeaderType> getHeaderTypes();
	void setData(String[] headers);
	void setPresenter(HeaderTypeFormPresenterImpl presenter);
	Widget asWidget();

}
