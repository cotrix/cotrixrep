package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.action.CodelistAction.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Collection;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.application.NewsService;
import org.cotrix.application.NewsService.NewsItem;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class NewsServiceTest {

	static NewsItem testItem = new NewsItem("text");
	
	@Inject
	NewsService service;
	
	@Inject
	LifecycleService lcService;
	
	@Inject
	TestReporter reporter;
	

	@Test
	public void publishNews() {
	
		reporter.fire(testItem);
		
		Collection<NewsItem> news = service.news();
		
		assertTrue(news.contains(testItem));
	}
	
	@Test
	public void getRecentNews() throws Exception {
	
		reporter.fire(new NewsItem("some"));
		reporter.fire(new NewsItem("some more"));

		Collection<NewsItem> news = service.news();
		
		System.out.println(news);
		
		//just to be sure
		Thread.sleep(100);

		Calendar now = Calendar.getInstance();
		
		NewsItem latest = new NewsItem("latest");
		
		reporter.fire(latest);
		
		news = service.newsSince(now);
		
		System.out.println(news);
		
		assertEquals(asList(latest),news);
	}
	
	@Test
	public void publishCodelistLifecycleNews() {
	
		Lifecycle lc = lcService.start("id");

		int size = service.news().size(); 

		lc.notify(EDIT.on("id"));
		
		assertEquals(size+1,service.news().size());
	}
	
	
	
	static class TestReporter {
		
		@Inject
		Event<NewsItem> news;
		
		void fire(NewsItem item) {
			news.fire(item);
		}
	}
	
}
