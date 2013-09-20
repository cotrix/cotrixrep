package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPanelView {
	public interface Presenter {

	}

	Widget asWidget();

	public abstract void setProvider(CodeListRowDataProvider dataProvider);

}
