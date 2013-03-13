package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;


public interface CodeListManagerView {
	public interface Presenter<T> {

	}
	void init();
	void setLeftPanel(IsWidget leftPanel);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
