package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.presenter.CodeListDetailPresenterImpl.OnNavigationClicked;
import org.cotrix.web.publish.client.view.CodeListDetailView;

public interface CodeListDetailPresenter extends Presenter<CodeListDetailPresenter>,CodeListDetailView.Presenter<CodeListDetailPresenter> {
	public void setOnNavigationLeftClicked(OnNavigationClicked onNavigationClicked);
	public void setData(int id);
}
