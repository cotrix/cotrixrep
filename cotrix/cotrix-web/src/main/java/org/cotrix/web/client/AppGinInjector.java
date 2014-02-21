package org.cotrix.web.client;

import org.cotrix.web.client.presenter.HomeController;
import org.cotrix.web.client.presenter.UserBarPresenter;
import org.cotrix.web.common.client.CommonGinModule;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({AppGinModule.class, CommonGinModule.class})
public interface AppGinInjector extends Ginjector {
	
	public static AppGinInjector INSTANCE = GWT.create(AppGinInjector.class);

	public AppController getAppController();
	
	public UserBarPresenter getPresenter();
	
	public HomeController getHomeController();
}
