package org.cotrix.web.client;

import java.util.EnumMap;

import org.cotrix.web.client.event.CotrixStartupEvent;
import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.client.presenter.HomeController;
import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.ingest.client.CotrixIngestGinInjector;
import org.cotrix.web.manage.client.CotrixManagerAppGinInjector;
import org.cotrix.web.menu.client.presenter.CotrixMenuGinInjector;
import org.cotrix.web.menu.client.presenter.MenuPresenter;
import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.users.client.CotrixPermissionGinInjector;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixController implements Presenter {
	
	protected EventBus cotrixBus;
	protected CotrixWebPresenter cotrixWebPresenter;
	
	protected EnumMap<CotrixModule, CotrixModuleController> controllers = new EnumMap<CotrixModule, CotrixModuleController>(CotrixModule.class);
	protected CotrixModuleController currentController;
	
	protected Timer statisticsUpdater;
	
	@Inject
	public CotrixController(@CotrixBus EventBus cotrixBus, CotrixWebPresenter cotrixWebPresenter, HomeController home) {
		this.cotrixWebPresenter = cotrixWebPresenter;
		this.cotrixBus = cotrixBus;
		
		CommonResources.INSTANCE.css().ensureInjected();
		
		initMenu();
		
		addModule(home);
		
		/*GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {*/
				CotrixIngestGinInjector importInjector = CotrixIngestGinInjector.INSTANCE;
				addModule(importInjector.getController());
			/*}
			
			@Override
			public void onFailure(Throwable reason) {
				Log.fatal("Import module not loaded", reason);
			}
		});*/
		
		/*GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {*/
				CotrixManagerAppGinInjector managerInjector = CotrixManagerAppGinInjector.INSTANCE;
				addModule(managerInjector.getController());
			/*}
			
			@Override
			public void onFailure(Throwable reason) {
				Log.fatal("Manager module not loaded", reason);
			}
		});*/
		
		/*GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {*/
				CotrixPublishAppGinInjector publishInjector = CotrixPublishAppGinInjector.INSTANCE;
				addModule(publishInjector.getController());
			/*}
			
			@Override
			public void onFailure(Throwable reason) {
				Log.fatal("Publish module not loaded", reason);
			}
		});*/
		
		/*GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {*/
				CotrixPermissionGinInjector permissionInjector = CotrixPermissionGinInjector.INSTANCE;
				addModule(permissionInjector.getController());
			/*}
			
			@Override
			public void onFailure(Throwable reason) {
				Log.fatal("Publish module not loaded", reason);
			}
		});*/

		
		showModule(CotrixModule.HOME);
		
		cotrixBus.fireEvent(new CotrixStartupEvent());
	}
	
	@Inject
	protected void bind()
	{
		cotrixBus.addHandler(SwitchToModuleEvent.TYPE, new SwitchToModuleEvent.SwitchToModuleHandler() {
			
			@Override
			public void onSwitchToModule(SwitchToModuleEvent event) {
				showModule(event.getModule());		
			}
		});
	}
	
	protected void initMenu()
	{
		CotrixMenuGinInjector menuInjector = CotrixMenuGinInjector.INSTANCE;
		MenuPresenter menuPresenter = menuInjector.getMenuPresenter();
		cotrixWebPresenter.setMenu(menuPresenter);
	}
	
	protected void addModule(CotrixModuleController controller)
	{
		cotrixWebPresenter.add(controller);
		controllers.put(controller.getModule(), controller);
	}
	
	protected void showModule(CotrixModule cotrixModule)
	{
		if (controllers.containsKey(cotrixModule)) {
		if (currentController!=null) currentController.deactivate();
		currentController = controllers.get(cotrixModule);
		cotrixWebPresenter.showModule(cotrixModule);
		currentController.activate();
		} else Log.warn("Missing module "+cotrixModule+" forgot to register it?");
	}

	public void go(HasWidgets container) {
		cotrixWebPresenter.go(container);
	}

}
