package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.view.CodeListPanelView;
import org.cotrix.web.share.shared.UICodelist;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPanelPresenter extends Presenter, CodeListPanelView.Presenter {
	
	public CodeListPanelView getView();

	public abstract void setCodeList(UICodelist codelist);
}
