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
import org.cotrix.web.importwizard.client.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.ImportWizardPresenterImpl;
import org.cotrix.web.importwizard.client.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepView;
import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.menu.client.view.MenuViewImpl;
import org.cotrix.web.publish.client.CotrixPublishAppController;
import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;

public class CotrixWebPresenterImpl implements CotrixWebPresenter {
	private MainServiceAsync rpcService;
	private HandlerManager eventBus;
	private CotrixWebView view;
	private CotrixPublishAppController cotrixPublishAppController;
	private CotrixManagerAppController cotrixManagerAppController;
	
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
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		CotrixImportAppGinInjector cotrixImportAppGinInjector = GWT.create(CotrixImportAppGinInjector.class);
		CotrixImportAppController cotrixImportAppController = cotrixImportAppGinInjector.getAppController();	
		cotrixImportAppController.go(scrollPanel);
		view.getBody().add(scrollPanel);

		CotrixManagerAppGinInjector cotrixManagerAppGinInjector = GWT.create(CotrixManagerAppGinInjector.class);
		cotrixManagerAppController = cotrixManagerAppGinInjector.getAppController();	
		cotrixManagerAppController.go(view.getBody());
	
		CotrixPublishAppGinInjector cotrixPublishAppGinInjector = GWT.create(CotrixPublishAppGinInjector.class);
		cotrixPublishAppController = cotrixPublishAppGinInjector.getAppController();
		cotrixPublishAppController.go(view.getBody());
		
		view.showMenu(0); // default menu;
	}

	public void onMenuItemClick(int index) {
		view.showMenu(index);
		if(index == 3){
			cotrixPublishAppController.refresh();
		}else if(index == 2){
			cotrixManagerAppController.refresh();
		}
	}

}
