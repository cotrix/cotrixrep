package org.cotrix.web.codelistmanager.client.view;

import java.util.ArrayList;

import org.cotrix.web.share.shared.Codelist;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;


public interface CodeListManagerView {
	public interface Presenter<T> {

	}

	HasWidgets getContentPanel();
	void showLeftPanel(boolean isShow);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
