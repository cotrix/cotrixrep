package org.cotrix.application.impl.news;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.action.events.CodelistActionEvents.Create;
import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.action.events.CodelistActionEvents.Publish;
import org.cotrix.action.events.CodelistActionEvents.Version;
import org.cotrix.application.NewsService.NewsItem;

public class CodelistActionReporter {

	@Inject
	private Event<NewsItem> news;
	
	public void onImport(@Observes Import e) throws Exception {
	
		news.fire(new NewsItem(e.codelistName+" version "+ e.codelistVersion+" now available."));
	
	}
	
	public void onVersion(@Observes Version e) throws Exception {
		
		news.fire(new NewsItem("version "+e.codelistVersion+" of "+e.codelistName+" now available."));
		
	}
	
	public void onPublish(@Observes Publish e) throws Exception {
		
		news.fire(new NewsItem(e.codelistName+" version "+ e.codelistVersion+" has just been published to "+e.repository+"."));
	}
	
	public void onCreate(@Observes Create e) throws Exception {
		
		news.fire(new NewsItem("Version "+ e.codelistVersion+" of "+e.codelistName+" has been created."));
	}
}
