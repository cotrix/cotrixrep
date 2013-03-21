package org.cotrix.web.client.presenter;

import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.menu.client.presenter.MenuPresenter.OnCotrixMenuItemClicked;

public interface CotrixWebPresenter extends Presenter<CotrixWebPresenter>,CotrixWebView.Presenter<CotrixWebPresenter>,OnCotrixMenuItemClicked{

}
