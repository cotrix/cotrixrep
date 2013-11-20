/**
 * 
 */
package org.cotrix.web.client.presenter;

import java.util.List;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.view.HomeView;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;
import org.cotrix.web.share.client.error.IgnoreFailureCallback;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.shared.UINews;
import org.cotrix.web.shared.UIStatistics;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
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
	
	protected Timer newsUpdater;
	
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
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				updateNews();				
			}
		});
		
		newsUpdater = new Timer() {
			
			@Override
			public void run() {
				updateNews();
			}
		};
		newsUpdater.scheduleRepeating(5*60*1000);
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
		service.getStatistics(new IgnoreFailureCallback<UIStatistics>() {
			
			@Override
			public void onSuccess(UIStatistics result) {
				view.setStatistics(result.getCodelists(), result.getCodes(), result.getUsers(), result.getRepositories());				
			}
		});
	}
	
	protected void updateNews() {
		service.getNews(new ManagedFailureCallback<List<UINews>>() {
			
			@Override
			public void onSuccess(List<UINews> result) {
				view.setNews(result);				
			}
		});
	}


	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());		
	}

}
