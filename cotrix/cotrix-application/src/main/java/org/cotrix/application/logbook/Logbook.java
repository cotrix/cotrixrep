package org.cotrix.application.logbook;

import static java.text.DateFormat.*;
import static java.util.Arrays.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("all")
public class Logbook implements Serializable {

	public static class Entry implements Serializable {
	
		private final String timestamp;
		private final LogbookEvent event;
		private String description;
		private final String user;
		
		Entry(LogbookEvent event,String user) {
			this.timestamp=getDateTimeInstance().format(Calendar.getInstance().getTime());
			this.event=event;
			this.user=user;
		}
		
		public Entry description(String description) {
			this.description = description;
			return this;
		}
		
		public String description() {
			return description;
		}
		
		public String timestamp() {
			return timestamp;
		}
		
		public LogbookEvent event() {
			return event;
		}
		
		public String user() {
			return user;
		}

		@Override
		public String toString() {
			return "Entry [timestamp=" + timestamp + ", event=" + event + ", description=" + description + ", user=" + user + "]";
		}
	
		
		
	}
	
	
	private final String resourceId;
	
	private final List<Entry> entries = new ArrayList<Logbook.Entry>();
	
	public Logbook(String resourceId){
		this.resourceId=resourceId;
	}
	
	public String resourceId() {
		return resourceId;
	}
	
	public synchronized List<Entry> entries() {
		return entries;
	}
	
	public synchronized List<Entry> entries(LogbookEvent event) {
		List<Entry> matches = new ArrayList<>();
		for (Entry e : entries)
			if (e.event==event)
				matches.add(e);
		return matches;
	}
	
	public synchronized Logbook add(Entry ... es) {
		entries.addAll(asList(es));
		return this;
	}
	
	public synchronized boolean remove(Entry e) {
		return entries.remove(e);
	}

	@Override
	public String toString() {
		return "Logbook [resourceId=" + resourceId + ", entries=" + entries + "]";
	}
	
	
	
	
	
}
