package org.cotrix.web.menu.client.presenter;

import org.cotrix.web.menu.client.view.MenuView;
import org.cotrix.web.menu.client.view.MenuViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixMenuGinModule extends AbstractGinModule {

	protected void configure() {
		bind(MenuView.class).to(MenuViewImpl.class).in(Singleton.class);
		bind(MenuPresenter.class).in(Singleton.class);
	}
}
