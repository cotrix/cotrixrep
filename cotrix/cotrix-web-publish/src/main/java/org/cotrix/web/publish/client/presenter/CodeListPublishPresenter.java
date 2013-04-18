package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.presenter.CodeListDetailPresenterImpl.OnNavigationClicked;
import org.cotrix.web.publish.client.presenter.CodeListPresenterImpl.OnCodelistItemClicked;
import org.cotrix.web.publish.client.view.CodeListPublishView;



public interface CodeListPublishPresenter extends Presenter<CodeListPublishPresenter> ,CodeListPublishView.Presenter<CodeListPublishPresenterImpl>,OnCodelistItemClicked,OnNavigationClicked{

}
