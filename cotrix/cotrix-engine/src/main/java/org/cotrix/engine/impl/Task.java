package org.cotrix.engine.impl;

import java.util.Calendar;
import java.util.concurrent.Callable;

import org.cotrix.action.Action;
import org.cotrix.domain.user.User;
import org.cotrix.engine.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps {@link Callable}s to provide metadata about their execution.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the task output
 */
public class Task<T>  {

	//logs on beahalf of Engine
	private static Logger log = LoggerFactory.getLogger(Engine.class);
	
	private final Callable<T> task;
	
	private final Calendar created;
	private Calendar started;
	private Calendar completed;
	
	/**
	 * Creates editAction task for editAction given {@link Callable}.
	 * @param function the {@link Callable}
	 * @return the task
	 */
	public static <T> Task<T> taskFor(Callable<T> function) {
		return new Task<T>(function);
	}
	
	//only through factory method
	private Task(Callable<T> task) {
		this.task=task;
		this.created=Calendar.getInstance();
	}
	
	/**
	 * Executes this task to perform editAction given {@link Action} on behalf of editAction given {@link User}
	 * @param action the action
	 * @return the output of the task
	 */
	public T execute(Action action) {
		
		this.started = Calendar.getInstance();
		
		log.info("performing {}",action);
		
		try {
			
			return task.call();
		}
		catch(Exception e) {
			throw new RuntimeException("cannot complete task "+task+" for "+action, e);
		}
		finally {
			this.completed = Calendar.getInstance();
			log.info("performed {} in {} ms.",action,completed.getTimeInMillis()-started.getTimeInMillis());
		}
		
	}
	
	public Calendar created() {
		return created;
	}
	
	public Calendar completed() {
		return completed;
	}
	
	public Calendar started() {
		return started;
	}
}
