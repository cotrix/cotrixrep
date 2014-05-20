package org.cotrix.web.client.main;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CotrixWebViewImpl.class)
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
