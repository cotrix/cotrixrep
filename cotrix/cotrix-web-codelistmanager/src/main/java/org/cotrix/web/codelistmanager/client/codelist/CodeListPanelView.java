package org.cotrix.web.codelistmanager.client.codelist;


import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPanelView {
	public interface Presenter {

	}

	Widget asWidget();
	
	CodeListToolbar getToolBar();

}
