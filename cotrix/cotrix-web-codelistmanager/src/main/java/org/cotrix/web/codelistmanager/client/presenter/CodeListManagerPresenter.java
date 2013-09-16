package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenterImpl.OnNavigationClicked;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListManagerPresenter extends Presenter, CodeListManagerView.Presenter, OnNavigationClicked, OnCodelistItemClicked{
	public void refresh();
}
