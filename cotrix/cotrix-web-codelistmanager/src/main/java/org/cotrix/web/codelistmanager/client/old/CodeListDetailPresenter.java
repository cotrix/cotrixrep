package org.cotrix.web.codelistmanager.client.old;

import org.cotrix.web.codelistmanager.client.Presenter;
import org.cotrix.web.codelistmanager.client.old.CodeListDetailPresenterImpl.OnNavigationClicked;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodeListDetailPresenter extends Presenter,CodeListDetailView.Presenter<CodeListDetailPresenter> {
	public void setOnNavigationLeftClicked(OnNavigationClicked onNavigationClicked);
	public void setData(String id);
}
