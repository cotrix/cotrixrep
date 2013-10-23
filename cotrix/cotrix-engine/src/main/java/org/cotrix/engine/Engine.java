package org.cotrix.engine;

import java.util.concurrent.Callable;

import org.cotrix.action.Action;

/**
 * Performs {@link Action}s by executing given tasks.
 * <p>
 * The engine represents the application, directly addressing its authorisation concerns and driving its transitions.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Engine {
	
	/**
	 * Performs a given action by executing a given task
	 * @param action the action
	 * @return the task selection clause
	 */
	TaskClause perform(Action action);
	
	
	/**
	 * Clause to select editAction task for executing editAction given action.
	 * 
	 */
	public static abstract class TaskClause {
		
		/**
		 * Performs the current action by executing a given task. 
		 * @param task the task
		 * @return the {@link TaskOutcome outcome} of the task
		 * 
		 * 
		 * @throws IllegalAccessError if the current action cannot be performed on behalf of the current user.
		 * @throws IllegalStateException if the current action cannot be performed on the target instance.
		 */
		public abstract <T> TaskOutcome<T> with(Callable<T> task);
		
		/**
		 * Performs the current action by executing a given task. 
		 * @param task the task
		 * @return the {@link TaskOutcome outcome} of the task
		 * 
		 * @throws IllegalAccessError if the current action cannot be performed on behalf of the current user.
		 * @throws IllegalStateException if the current action cannot be performed on the target instance.
		 */
		public TaskOutcome<Void> with(Runnable task) {
			
			return with(asCallable(task));
		}
		
		private Callable<Void> asCallable(final Runnable task) {
			return new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					task.run();
					return null;
				}
			};
		}
	}
	
}
