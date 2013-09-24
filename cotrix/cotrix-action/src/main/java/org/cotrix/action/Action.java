package org.cotrix.action;

import java.util.List;

/**
 * An action that may be taken by the application.
 * <p>
 * Actions are multi-part names, optionally relative to a target instance.
 *  
 * @author Fabio Simeoni
 *
 */
public interface Action {

	public static final String action_part_separator =":";
	public static final String action_instance_separator ="@";
	
	/**
	 * The wildcard for an action's part.
	 */
	public static final String any ="*";
	
	/**
	 * Returns the parts of this action.
	 * @return the parts
	 */
	List<String> parts();
	
	/**
	 * Returns the identifier of the target instance for this action.
	 * @return the target instance, or <code>null</code> if the action is not relative to an instance.
	 */
	String instance();
	
	/**
	 * Returns <code>true</code> if this action is on an instance.
	 * @return <code>true</code> if this action is on an instance
	 */
	boolean isOnInstance();
	
	
	/**
	 * Returns <code>true</code> if this action is or specialises one of a list of actions.
	 * @param actions the actions
	 * @return <code>true</code> if this action is or specialises one of the actions in input.
	 */
	boolean isIn(Action ...actions);
}
