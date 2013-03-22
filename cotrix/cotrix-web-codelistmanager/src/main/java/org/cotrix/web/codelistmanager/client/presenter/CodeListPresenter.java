package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.codelistmanager.client.view.CodeListView;

public interface CodeListPresenter extends Presenter<CodeListPresenter> , CodeListView.Presenter<CodeListPresenter> {
	void setOnCodelistItemClicked(OnCodelistItemClicked onCodelistItemClicked);
}
