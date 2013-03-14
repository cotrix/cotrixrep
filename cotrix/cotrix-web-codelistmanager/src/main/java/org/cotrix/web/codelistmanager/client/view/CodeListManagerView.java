package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;


public interface CodeListManagerView {
	public interface Presenter<T> {

	}
	void init();
	HasWidgets getRightPanel();
	HasWidgets getLeftPanel();
	void showLeftPanel(boolean isShow);
	void expandRightPanel(boolean isExpand);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
