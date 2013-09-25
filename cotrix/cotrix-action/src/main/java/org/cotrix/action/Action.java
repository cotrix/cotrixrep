package org.cotrix.action;

import java.util.Collection;
import java.util.List;

/**
 * An action that may be taken by the application.
 * <p>
 * Actions have lists of one or more parts and are optionally relative to some target instance. The parts are plain strings,
 * with {@link #any} standing for any possible string. This defines a notion of <em>inclusion</em> between
 * actions whereby an action <code>A</code> includes another action <code>B</code> if:
 * 
 * <ul>
 * <li><code>A</code> and <code>B</code> operates on no instance or on the same instance;
 * <li>a part of <code>A</code> which is not {@link #any} is equals to the corresponding parts of <code>B</code>;
 * <ul>
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Action {

	/**
	 * The wildcard for an action's part.
	 */
	public static final String any = "*";

	/**
	 * Returns the parts of this action.
	 * 
	 * @return the parts
	 */
	List<String> parts();

	/**
	 * Returns the identifier of the target instance for this action.
	 * 
	 * @return the target instance, or <code>null</code> if the action is not relative to an instance.
	 */
	String instance();

	/**
	 * Returns <code>true</code> if this action is on an instance.
	 * 
	 * @return <code>true</code> if this action is on an instance
	 */
	boolean isOnInstance();

	/**
	 * Returns <code>true</code> if this action is or specialises one of a list of actions.
	 * 
	 * @param actions the actions
	 * @return <code>true</code> if this action is or specialises one of the actions in input.
	 */
	boolean isIn(Action... actions);
	
	/**
	 * Returns <code>true</code> if this action is or specialises one of a list of actions.
	 * 
	 * @param actions the actions
	 * @return <code>true</code> if this action is or specialises one of the actions in input.
	 */
	boolean isIn(Collection<Action> actions);
	
	/**
	 * Returns an action with the same parts as this action but on a given instance.
	 * @param instance the instance.
	 * @return
	 */
	Action cloneFor(String instance);
}
