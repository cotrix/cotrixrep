package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface CodeListDetailView {
	public interface Presenter<T> {
		void onNavLeftClicked(boolean isShowingNavLeft);

	}
	void init();
	void setPresenter(Presenter presenter);
	void onNavLeftClicked(ClickEvent event);
	void showNavLeft();
	void showNavRight();
	Widget asWidget();
}
