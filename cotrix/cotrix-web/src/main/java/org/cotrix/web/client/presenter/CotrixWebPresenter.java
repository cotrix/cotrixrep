package org.cotrix.web.client.presenter;

import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;

public interface CotrixWebPresenter extends Presenter<CotrixWebPresenter>, CotrixWebView.Presenter<CotrixWebPresenter> {
	
	public void setMenu(MenuPresenter menuView);
	public void add(CotrixModuleController moduleController);
	public void showModule(CotrixModule module);
}
