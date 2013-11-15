package org.cotrix.application.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.cotrix.application.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DefaultNewsService implements NewsService {

	private static final Logger log = LoggerFactory.getLogger(NewsService.class);
	
	private static final int NEWS_SIZE = 20;
	
	@SuppressWarnings("serial")
	private final LinkedHashMap<Integer,NewsItem> news = new LinkedHashMap<Integer,NewsItem>(NEWS_SIZE) {
		
		protected boolean removeEldestEntry(Entry<Integer,NewsService.NewsItem> eldest) {
			return size()>NEWS_SIZE;
		};
	};
	

	public synchronized Collection<NewsItem> news() {
		return news.values();
	};
	
	@Override
	public synchronized Collection<NewsItem> newsSince(Calendar date) {
		
		Collection<NewsItem> recent = new ArrayList<NewsItem>();
		
		for (NewsItem item : news.values())
			if (item.after(date))
				recent.add(item);	
		
		return recent;
	}
	
	@SuppressWarnings("unused") //CDI invokes this
	private synchronized void monitor(@Observes NewsItem item) {
		
		log.info("publishing news item "+item);
		
		news.put(item.hashCode(),item);
	}
}
