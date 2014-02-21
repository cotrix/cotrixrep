package org.cotrix.application.impl.news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.application.NewsService.NewsItem;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReleaseNewsReporter {

	private static final String RELEASE_NEWS_FILE = "/news.txt";

	private static Logger log = LoggerFactory.getLogger(ReleaseNewsReporter.class);
	
	@Inject
	private Event<NewsItem> news;
	
	public void start(@Observes Startup event) throws Exception {
	
		log.info("reading release news");
		
		InputStream config = ReleaseNewsReporter.class.getResourceAsStream(RELEASE_NEWS_FILE);
			
		if (config==null)
			throw new IllegalStateException("application is misconfigured: no "+RELEASE_NEWS_FILE+" news file");
			
		BufferedReader reader = new BufferedReader(new InputStreamReader(config));
		
		String item = null;

		while ((item = reader.readLine())!=null)
			news.fire(new NewsItem(item));
	
	}
	
	
}
