package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface CodeListDetailView {
	public interface Presenter<T> {

	}
	void init();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
