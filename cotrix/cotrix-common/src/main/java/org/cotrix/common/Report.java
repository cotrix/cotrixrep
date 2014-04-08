package org.cotrix.common;

import static org.cotrix.common.Log.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.common.Report.Item.Type;

public class Report {

	public static abstract class Item {
		
		public static enum Type {WARN,ERROR,INFO}
		
		private Type type = Type.INFO;
		private String time;
		
		public abstract String message();
		
		public void time(String time) {
			this.time=time;
		}
		
		public String time() {
			return time;
		}
		
		public Type type() {
			return type;
		}
		
		public void type(Type type) {
			this.type=type;
		}
		
		public <T extends Item> T as(Class<T> type) {
			return type.cast(this);
		}
	}
	
	//report registry
	private static InheritableThreadLocal<Report> reports = new InheritableThreadLocal<Report>() {
		
		protected Report initialValue() {
			return new Report();
		};
	};
	
	/**
	 * Returns the report currently ongoing in the current thread.
	 * @return the current report
	 */
	public static Report report() {
	
		return (Report) reports.get();
	
	}
	
	//used in log() sentences
	public static interface TypeClause {
		
		Report as(Type type);
	
	}
	
	private final double start = System.currentTimeMillis();
	
	private List<Item> items = new ArrayList<Item>();
	
	private boolean failure;
	
	
	
	public Report() {}
	
	/**
	 * Returns the log messages in this report.
	 * @return the messages
	 */
	public List<Item> logs() {
		return items;
	}
	
	public TypeClause log(String msg) {
	
		return log(item(msg));
	}
	
	public TypeClause log(final Item item) {
		
		notNull("item",item);
		
		item.time(time());
		
		items.add(item);
		
		return new TypeClause() {
			
			@Override
			public Report as(Type type) {
				
				item.type(type);
				
				if (type==Type.ERROR)
					failure=true;
				
				return Report.this;
			}
		};

	}
	
	/**
	 * Closes the report.
	 */
	public void close() {
		reports.remove(); //cleanup thread-local
	}
	
	/**
	 * Returns <code>true</code> if the report contains errors.
	 * @return <code>true</code> if the report contains errors.
	 */
	public boolean isFailure() {
		return failure;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		for (Item item : items)
			builder.append(item.type()+":")
				   .append(" ("+item.time()+"s) ")
				   .append(item.message())
				   .append("\n");
		
		return builder.toString();
	}
	
	public String time() {
		return String.valueOf((System.currentTimeMillis()-start)/1000);
	}
	

}
