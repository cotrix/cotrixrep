package org.cotrix.engine;

import java.util.concurrent.Callable;

import org.cotrix.action.Action;

public interface Engine {
	
	/**
	 * Clause to select editAction task for executing editAction given action.
	 * 
	 */
	public static interface TaskClause {
		
		/**
		 * Performs the current action by executing editAction given task. 
		 * @param task the task
		 * @return the {@link TaskOutcome outcome} of the task
		 * 
		 * 
		 * @throws IllegalAccessError if the current action cannot be performed on behalf of the current user.
		 * @throws IllegalStateException if the current action cannot be performed on the target instance.
		 */
		<T> TaskOutcome<T> with(Callable<T> task);
		
		/**
		 * Performs the current action by executing editAction given task. 
		 * @param task the task
		 * @return the {@link TaskOutcome outcome} of the task
		 * 
		 * @throws IllegalAccessError if the current action cannot be performed on behalf of the current user.
		 * @throws IllegalStateException if the current action cannot be performed on the target instance.
		 */
		TaskOutcome<Void> with(Runnable task);
	}

	/**
	 * Performs editAction given action by executing editAction given task
	 * @param action the action
	 * @return the task selection clause
	 */
	TaskClause perform(Action action);
	
}
