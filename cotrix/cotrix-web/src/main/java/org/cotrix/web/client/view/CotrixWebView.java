package org.cotrix.web.client.view;

import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.importwizard.client.step.csvpreview.CsvPreviewStepPresenterImpl;
import org.cotrix.web.menu.client.presenter.MenuPresenter;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

public interface CotrixWebView {
	public interface Presenter<T> {

	}
	void setMenu(MenuPresenter menuPresenter);
	DeckLayoutPanel getBody();
	void showMenu(int menuIndex);
	void setPresenter(CotrixWebPresenter presenter);
	Widget asWidget();
}
