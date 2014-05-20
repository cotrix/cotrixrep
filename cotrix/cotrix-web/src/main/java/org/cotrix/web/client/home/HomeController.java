/**
 * 
 */
package org.cotrix.web.client.home;

import java.util.List;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.error.IgnoreFailureCallback;
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
	
	private static final int POLLING_DELAY = 5*60*1000;
	
	@Inject
	private MainServiceAsync service;
	
	@Inject
	private HomeView view; 
	
	private boolean firstStatisticsLoaded = false;
	private Timer statisticsUpdater;
	
	private boolean firstNewsLoaded = false;
	private Timer newsUpdater;
	
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
		statisticsUpdater.scheduleRepeating(POLLING_DELAY);
		
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
		newsUpdater.scheduleRepeating(POLLING_DELAY);
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
	
	private void updateStatistics() {
		service.getStatistics(new IgnoreFailureCallback<UIStatistics>() {
			
			@Override
			public void onSuccess(UIStatistics result) {
				view.setStatistics(result.getCodelists(), result.getCodes(), result.getUsers(), result.getRepositories());
				firstStatisticsLoaded = true;
			}
			
			public void failed() {
				if (!firstStatisticsLoaded) view.setStatisticsNotAvailable();
			}
		});
	}
	
	private void updateNews() {
		service.getNews(new IgnoreFailureCallback<List<UINews>>() {
			
			@Override
			public void onSuccess(List<UINews> result) {
				firstNewsLoaded = true;
				view.setNews(result);				
			}
			
			public void failed() {
				if (!firstNewsLoaded) view.setNewsNotAvailable();
			}
		});
	}


	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());		
	}
}
