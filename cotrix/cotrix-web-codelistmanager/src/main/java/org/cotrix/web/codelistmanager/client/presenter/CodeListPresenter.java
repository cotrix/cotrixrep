package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.codelistmanager.client.view.CodeListView;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListPresenter extends Presenter<CodeListPresenter> , CodeListView.Presenter<CodeListPresenter> {
	void setOnCodelistItemClicked(OnCodelistItemClicked onCodelistItemClicked);
	void refresh();
}
