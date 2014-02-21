package org.cotrix.web.client.view;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public interface CotrixWebView {
	public interface Presenter {
		public void onTitleAreaClick();
	}
	
	public void setPresenter(Presenter presenter);
	
	FlowPanel getMenuPanel();
	DeckLayoutPanel getModulesPanel();
	
	void showModule(int moduleIndex);

	Widget asWidget();
	FlowPanel getUserBarPanel();
}
