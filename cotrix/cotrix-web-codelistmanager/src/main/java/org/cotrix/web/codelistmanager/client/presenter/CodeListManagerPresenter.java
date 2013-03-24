package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenterImpl.OnNavigationClicked;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;

public interface CodeListManagerPresenter extends Presenter<CodeListManagerPresenter> ,CodeListManagerView.Presenter<CodeListManagerPresenterImpl>,OnNavigationClicked,OnCodelistItemClicked{

}
