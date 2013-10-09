package org.cotrix.web.codelistmanager.client.codelist;


import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistPanelView {
	public interface Presenter {

	}

	Widget asWidget();
	
	CodelistToolbar getToolBar();
	
	CodelistEditor getCodeListEditor();

}
