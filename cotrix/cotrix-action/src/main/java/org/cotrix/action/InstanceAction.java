package org.cotrix.action;

/**
 * An action over a given instance.
 * 
 * @author Fabio Simeoni
 *
 */
public interface InstanceAction extends Action {

	/**
	 * Returns the identifier of this action's instance.
	 * 
	 * @return the identifier of the instance
	 */
	String instance();
	
	/**
	 * Returns the action that has the same parts as this action but no target instance.
	 * @return the action
	 */
	Action onAny();
}
