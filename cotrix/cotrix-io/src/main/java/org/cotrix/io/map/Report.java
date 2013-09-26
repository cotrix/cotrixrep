package org.cotrix.io.map;

import static org.cotrix.common.Utils.*;
import static org.cotrix.io.map.Report.Log.*;

import java.util.ArrayList;
import java.util.List;


/**
 * A report on the mapping of a codelist onto a domain object.
 * <p>
 * Reports are thread-locals.
 * 
 * @author Fabio Simeoni
 *
 */
public class Report {

	//report registry
	private static InheritableThreadLocal<Report> reports = new InheritableThreadLocal<Report>() {
		
		protected Report initialValue() {
			return new Report();
		};
	};
	
	/**
	 * Returns the current report.
	 * @return
	 */
	public static Report report() {
		return reports.get();
	}
	
	private final double start = System.currentTimeMillis();
	private List<Log> logs = new ArrayList<Log>();
	
	private boolean failure;
	
	//create only throuugh factory method
	private Report() {}
	
	/**
	 * Returns the log messages in this report.
	 * @return the messages
	 */
	public List<Log> logs() {
		return logs;
	}
	
	public void log(String message,Log.Type type) {
		valid("message",message);
		logs.add(get("["+time()+"s] "+message,type));
	}
	
	/**
	 * Adds a message to this report.
	 * @param message the message
	 */
	public void log(String message) {
		log(message,Log.Type.INFO);
	}
	
	/**
	 * Adds a warning message to this report.
	 * @param message the report
	 */
	public void logWarning(String message) {
		log(message,Log.Type.WARN);;
	}
	
	/**
	 * Adds an error message to this report.
	 * @param message the report
	 */
	public void logError(String message) {		
		log(message,Log.Type.ERROR);
		failure=true;
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
		
		for (Log log : logs)
			builder.append(log).
					append("\n");
		
		return builder.toString();
	}
	
	private double time() {
		return (System.currentTimeMillis()-start)/1000;
	}
	
	/**
	 * A log message in a {@link Report}.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Log {
		
		/**
		 * Log message types
		 * @author Fabio Simeoni
		 *
		 */
		public static enum Type {WARN,ERROR,INFO}
		
		/**
		 * Returns a new log of a given type.
		 * @param msg the log message
		 * @param type the log type
		 * @return the log
		 */
		public static Log get(String msg, Type type) {
			return new Log(msg,type);
		}
		
		final String msg;
		final Type type; 
		
		private Log(String msg,Type type) {
			this.msg=msg;
			this.type=type;
		}


		public String message() {
			return msg;
		}

		public Type type() {
			return type;
		}
		
		@Override
		public String toString() {
			return type+":"+msg;
		}
		
	}
}
