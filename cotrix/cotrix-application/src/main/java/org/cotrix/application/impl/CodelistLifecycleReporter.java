package org.cotrix.application.impl;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.application.NewsService.NewsItem;
import org.cotrix.domain.Codelist;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.repository.CodelistRepository;

public class CodelistLifecycleReporter {

	@Inject
	CodelistRepository repository;
	
	@Inject
	private Event<NewsItem> news;
	
	public void start(@Observes LifecycleEvent e) throws Exception {
		
		if (e.origin().equals(e.target()))
			return;
		
		Codelist list = repository.lookup(e.resourceId());	
		
		news.fire(new NewsItem("codelist "+list.name()+" has moved to state "+e.target()));
	
	}
	
	
}
