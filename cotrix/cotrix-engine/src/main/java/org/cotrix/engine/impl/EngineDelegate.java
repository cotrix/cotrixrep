package org.cotrix.engine.impl;

import java.util.Collection;

import org.cotrix.action.ResourceType;
import org.cotrix.action.Action;
import org.cotrix.engine.TaskOutcome;

/**
 * Performs actions of given types on behalf of the {@link DefaultEngine}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface EngineDelegate {

	/**
	 * Returns the action types handled by this delegate 
	 * @return
	 */
	ResourceType type();
	
	/**
	 * Performs an action with a given task for a given user.
	 * @param action the action to perform
	 * @param task the task to perform the action with
	 * @param user the user
	 * @param permissions the user's permissions
	 * @return the outcome of the task
	 */
	<T> TaskOutcome<T> perform(Action action, Task<T> task, String user, Collection<Action> permissions);
}
