package org.cotrix.web.client.presenter;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.client.view.Home;
import org.cotrix.web.client.view.Publish;
import org.cotrix.web.codelistmanager.client.CotrixManagerAppController;
import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.importwizard.client.CotrixImportAppController;
import org.cotrix.web.importwizard.client.CotrixImportAppGinInjector;
import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenterImpl;
import org.cotrix.web.importwizard.client.view.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.menu.client.view.MenuViewImpl;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class CotrixWebPresenterImpl implements CotrixWebPresenter {
	private MainServiceAsync rpcService;
	private HandlerManager eventBus;
	private CotrixWebView view;
	
	@Inject
	public CotrixWebPresenterImpl(MainServiceAsync rpcService, HandlerManager eventBus, CotrixWebView view) {
			this.rpcService = rpcService;
			this.eventBus = eventBus;
			this.view = view;
			this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		init();
	}
	
	private void init(){
		MenuViewImpl menuView = new MenuViewImpl();
		MenuPresenter menuPresenter = new MenuPresenter(eventBus, menuView);
		menuPresenter.setOnCotrixMenuItemClicked(this);
		view.setMenu(menuPresenter);
		
		Home home  = new Home();
		view.getBody().add(home);
	
		CotrixImportAppGinInjector cotrixImportAppGinInjector = GWT.create(CotrixImportAppGinInjector.class);
		CotrixImportAppController cotrixImportAppController = cotrixImportAppGinInjector.getAppController();	
		cotrixImportAppController.go(view.getBody());

		CotrixManagerAppGinInjector cotrixManagerAppGinInjector = GWT.create(CotrixManagerAppGinInjector.class);
		CotrixManagerAppController cotrixManagerAppController = cotrixManagerAppGinInjector.getAppController();	
		cotrixManagerAppController.go(view.getBody());
	
		Publish publish  = new Publish();
		view.getBody().add(publish);
		
		view.showMenu(0); // default menu;
	}

	public void onMenuItemClick(int index) {
		view.showMenu(index);
	}

}
