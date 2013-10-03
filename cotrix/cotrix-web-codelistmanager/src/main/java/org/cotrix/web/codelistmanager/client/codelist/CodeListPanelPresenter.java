package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.Presenter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPanelPresenter extends Presenter, CodeListPanelView.Presenter {
	
	public CodeListPanelView getView();
}
