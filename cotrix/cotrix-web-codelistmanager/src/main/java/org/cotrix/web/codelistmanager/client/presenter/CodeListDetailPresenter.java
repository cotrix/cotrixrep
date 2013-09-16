package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenterImpl.OnNavigationClicked;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListDetailPresenter extends Presenter,CodeListDetailView.Presenter<CodeListDetailPresenter> {
	public void setOnNavigationLeftClicked(OnNavigationClicked onNavigationClicked);
	public void setData(String id);
}
