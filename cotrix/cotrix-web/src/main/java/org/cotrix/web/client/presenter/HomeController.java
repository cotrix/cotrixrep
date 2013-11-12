/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.view.HomeView;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;
import org.cotrix.web.shared.UIStatistics;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class HomeController implements CotrixModuleController {
	
	@Inject
	protected MainServiceAsync service;
	
	@Inject
	protected HomeView view; 
	
	protected Timer statisticsUpdater;
	
	public HomeController() {
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				updateStatistics();				
			}
		});
		
		statisticsUpdater = new Timer() {
			
			@Override
			public void run() {
				updateStatistics();
			}
		};
		statisticsUpdater.scheduleRepeating(5*60*1000);
	}
	
	@Override
	public CotrixModule getModule() {
		return CotrixModule.HOME;
	}
	
	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}
	
	protected void updateStatistics() {
		service.getStatistics(new AsyncCallback<UIStatistics>() {
			
			@Override
			public void onSuccess(UIStatistics result) {
				view.setStatistics(result.getCodelists(), result.getCodes(), result.getUsers(), result.getRepositories());				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting statistics", caught);
				
			}
		});
	}


	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());		
	}

}
