package org.cotrix.web.manage.client.manager;


import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodelistManagerViewImpl.class)
public interface CodelistManagerView {

	HasWidgets getWestPanel();
	void showWestPanel(boolean isShow);
	
	ContentPanel getContentPanel();
	
	Widget asWidget();
	void showAlert(String message);
}
