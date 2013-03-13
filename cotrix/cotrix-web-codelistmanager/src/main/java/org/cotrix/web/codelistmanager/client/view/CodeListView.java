package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.presenter.Presenter;

import com.google.gwt.user.client.ui.Widget;

public interface CodeListView{
	public interface Presenter<T> {

	}
	void init();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
