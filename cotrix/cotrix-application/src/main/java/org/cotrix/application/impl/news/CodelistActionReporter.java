package org.cotrix.application.impl.news;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.action.events.CodelistActionEvents.Publish;
import org.cotrix.application.NewsService.NewsItem;

public class CodelistActionReporter {

	@Inject
	private Event<NewsItem> news;
	
	public void onImport(@Observes Import e) throws Exception {
	
		news.fire(new NewsItem(e.name+" version "+ e.version+" now available."));
	
	}
	
	public void onVersion(@Observes Publish e) throws Exception {
		
		news.fire(new NewsItem("version "+e.version+" of "+e.name+" now available."));
		
	}
	
	public void onPublish(@Observes Publish e) throws Exception {
		
		news.fire(new NewsItem(e.name+" version "+ e.version+" has just been published to "+e.repository+"."));
	}

}
