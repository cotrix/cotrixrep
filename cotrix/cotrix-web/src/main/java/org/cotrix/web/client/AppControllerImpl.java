package org.cotrix.web.client;

import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.CotrixImportAppController;
import org.cotrix.web.importwizard.client.CotrixImportAppGinInjector;
import org.cotrix.web.menu.client.view.MenuViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class AppControllerImpl implements AppController {
	private HandlerManager eventBus;
	private MainServiceAsync rpcService;
	private HasWidgets container;
	private CotrixWebPresenter cotrixWebPresenter;
	
	@Inject
	public AppControllerImpl(HandlerManager eventBus,MainServiceAsync rpcService,CotrixWebPresenter cotrixWebPresenter) {
		this.eventBus = new HandlerManager(null);
		this.rpcService = rpcService;
		this.cotrixWebPresenter = cotrixWebPresenter;
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
		
		cotrixWebPresenter.go(container);
	}

}
