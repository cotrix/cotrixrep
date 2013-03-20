package org.cotrix.web.client;

import org.cotrix.web.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.CotrixImportAppController;
import org.cotrix.web.importwizard.client.CotrixImportAppGinInjector;
import org.cotrix.web.menu.client.view.MenuViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter {
	private final HandlerManager eventBus;
	private HasWidgets container;

	public AppController(HandlerManager eventBus) {
		this.eventBus = new HandlerManager(null);
	}

	private FlowPanel initPanel() {

		FlowPanel panel = new FlowPanel();
		FlowPanel content = new FlowPanel();
		MenuViewImpl menu = new MenuViewImpl();

		CotrixImportAppGinInjector cotrixImportInjector = GWT.create(CotrixImportAppGinInjector.class);
		CotrixImportAppController  cotrixImportAppController = cotrixImportInjector.getAppController();
		cotrixImportAppController.go(content);

		panel.add(menu);
		panel.add(content);
		return panel;
	}

	public void go(HasWidgets container) {
		this.container = container;
		this.container.add(initPanel());
	}

}
