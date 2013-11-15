package org.cotrix.application.impl;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.application.NewsService.NewsItem;
import org.cotrix.lifecycle.LifecycleEvent;

public class CodelistLifecycleReporter {

	@Inject
	private Event<NewsItem> news;
	
	public void start(@Observes LifecycleEvent e) throws Exception {
	
		news.fire(new NewsItem("codelist "+e.resourceId()+" has moved to state "+e.target()));
	
	}
	
	
}
