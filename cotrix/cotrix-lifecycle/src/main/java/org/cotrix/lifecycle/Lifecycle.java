package org.cotrix.lifecycle;

import java.io.Serializable;
import java.util.Collection;

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
