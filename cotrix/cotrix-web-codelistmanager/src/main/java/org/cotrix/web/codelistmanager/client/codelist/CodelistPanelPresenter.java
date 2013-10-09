package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.Presenter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistPanelPresenter extends Presenter, CodelistPanelView.Presenter {
	
	public CodelistPanelView getView();
}
