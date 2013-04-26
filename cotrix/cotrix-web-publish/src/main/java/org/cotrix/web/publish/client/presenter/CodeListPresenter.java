package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.publish.client.view.CodeListView;


public interface CodeListPresenter extends Presenter<CodeListPresenter> , CodeListView.Presenter<CodeListPresenter> {
	public void setOnCodelistItemClicked(OnCodelistItemClicked onCodelistItemClicked);
	public void refresh();
}
