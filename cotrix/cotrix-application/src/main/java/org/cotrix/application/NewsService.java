package org.cotrix.application;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Publishes news about the application.
 * 
 * @author Fabio Simeoni
 *
 */
public interface NewsService {

	
	/**
	 * Returns all the available news.
	 * @return the news 
	 */
	Collection<NewsItem> news();
	
	
	/**
	 * Returns all the available news since a given date.
	 * @param date the date
	 * @return the news 
	 */
	Collection<NewsItem> newsSince(Calendar date);

	
	
	
	public static class NewsItem {
		
		private static DateFormat format = DateFormat.getDateTimeInstance();
		
		private final String text;
		private final long timestamp;
		
		public NewsItem(String text) {
			this(text,Calendar.getInstance());
		}
		
		public NewsItem(String text,Calendar timestamp) {
			this.text=text;
			this.timestamp=timestamp.getTimeInMillis();
		}
		
		public String text() {
			return text;
		}
		
		public Date timestamp() {
			return new Date(timestamp);
		}
		
		public boolean after(Calendar d) {
			return timestamp>=d.getTimeInMillis();
		}
		
		@Override
		public String toString() {
			return "'"+text+"' ("+format.format(new Date(timestamp))+")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((text == null) ? 0 : text.hashCode());
			result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NewsItem other = (NewsItem) obj;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			if (timestamp != other.timestamp)
				return false;
			return true;
		}

		
		
		
	}
	
	

}
