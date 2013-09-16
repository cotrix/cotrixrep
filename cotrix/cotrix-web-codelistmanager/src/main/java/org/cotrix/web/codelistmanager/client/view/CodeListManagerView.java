package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListManagerView {
	public interface Presenter {

	}

	HasWidgets getWestPanel();
	void showWestPanel(boolean isShow);
	
	HasWidgets getCenterPanel();
	
	Widget asWidget();
}
