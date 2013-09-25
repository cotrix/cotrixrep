package org.cotrix.lifecycle;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.enterprise.event.Event;

import org.cotrix.action.Action;

/**
 * The lifecycle of a resource.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Lifecycle extends Serializable {

	/**
	 * The final clause of a {@link Lifecycle#perform(Action)} sentence.
	 * 
	 * @throw RuntimeException if the task could not be executed
	 */
	public interface TaskClause {
		
		/**
		 * Specifies the task that performs the action
		 * @param task the task
		 * @return the result of the task
		 * 
		 * @throws RuntimeException if the taks cannot be executed
		 */
		<T> T with(Callable<T> task);
	}
	
	/**
	 * Returns the identifier of the resource bound to this lifecyclye.
	 * @return the resource identifier
	 */
	String resourceId();

	/**
	 * Specifies the name of this lifecycle.
	 * @return the name of this lifecycle
	 */
	String name();
	
	/**
	 * Returns the current state of this lifecycle.
	 * @return the current state of this lifecycle
	 */
	State state();
	
	/**
	 * Returns <code>true</code> if a given action can be performed in the current state of this lifecycle.
	 * @param action the action
	 * @return <code>true</code> if the given action can be performed in the current state of this lifecycle
	 */
	boolean allows(Action action);
	
	/**
	 * Returns the actions that can be performed in the current state of this lifecycle.
	 * @return the actions that can be performed in the current state of this lifecycle
	 */
	Collection<Action> allowed();
	
	/**
	 * Notifies this lifecycle that a given action has been performed.
	 * @param action the action
	 */
	void notify(Action action);
	
	/**
	 * Sets an event producer on this lifecycle
	 * @param producer the producer
	 */
	void setEventProducer(Event<LifecycleEvent> producer);
}
