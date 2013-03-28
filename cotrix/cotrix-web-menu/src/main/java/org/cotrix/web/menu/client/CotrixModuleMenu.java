package org.cotrix.web.menu.client;

import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.menu.client.resources.CotrixMenuResources;
import org.cotrix.web.menu.client.view.MenuViewImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CotrixModuleMenu implements EntryPoint {
	public void onModuleLoad() {
		CotrixMenuResources.INSTANCE.css().ensureInjected();
		
		/*HandlerManager eventBus = new HandlerManager(null);
		MenuViewImpl menuView = new MenuViewImpl();
		MenuPresenter menuPresenter = new MenuPresenter(eventBus, menuView);
		menuPresenter.go(RootPanel.get());*/
	}
}
