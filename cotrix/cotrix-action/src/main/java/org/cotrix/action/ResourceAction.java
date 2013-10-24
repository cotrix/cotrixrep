package org.cotrix.action;

/**
 * An action over a given resource.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ResourceAction extends Action {

	/**
	 * Returns the identifier of this action's resource.
	 * 
	 * @return the identifier of the instance
	 */
	String resource();
	
	/**
	 * Returns the action that has the same parts as this action but no target resource.
	 * @return the action
	 */
	Action onAny();

}
