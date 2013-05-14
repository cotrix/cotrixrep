package org.cotrix.web.importwizard.client.view.form;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenterImpl;
import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.user.client.ui.Widget;

public interface HeaderTypeFormView<T> {
	public interface Presenter<T> {

	}
	ArrayList<HeaderType> getHeaderTypes();
	void setData(String[] headers);
	void setPresenter(HeaderTypeFormPresenterImpl presenter);
	void setStyleError();
	Widget asWidget();

}
