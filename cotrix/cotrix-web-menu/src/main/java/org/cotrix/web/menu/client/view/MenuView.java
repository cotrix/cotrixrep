package org.cotrix.web.menu.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface MenuView {
	public interface Presenter {

	}
	void showMenu();
	void setPresenter(Presenter presenter);
	Widget asWidget();
}