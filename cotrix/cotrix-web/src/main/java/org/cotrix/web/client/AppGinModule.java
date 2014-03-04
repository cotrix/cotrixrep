package org.cotrix.web.client;

import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.client.presenter.CotrixWebPresenterImpl;
import org.cotrix.web.client.presenter.UserController;
import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.client.view.CotrixWebViewImpl;
import org.cotrix.web.client.view.HomeView;
import org.cotrix.web.client.view.HomeViewImpl;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.client.view.UserBarViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AppGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AppController.class).to(AppControllerImpl.class);

		bind(CotrixWebView.class).to(CotrixWebViewImpl.class);
		bind(CotrixWebPresenter.class).to(CotrixWebPresenterImpl.class);
		
		bind(UserBarView.class).to(UserBarViewImpl.class).in(Singleton.class);
		
		bind(UserController.class).asEagerSingleton();
		
		bind(HomeView.class).to(HomeViewImpl.class);
	}

}
