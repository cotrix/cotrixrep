package org.cotrix.web.manage.client.codelist;

import org.cotrix.web.manage.client.Presenter;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistPanelPresenter extends Presenter, CodelistPanelView.Presenter {
	
	public CodelistPanelView getView();
}
