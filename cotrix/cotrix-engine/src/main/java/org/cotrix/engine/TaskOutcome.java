package org.cotrix.engine;

import java.util.Collection;

import org.cotrix.action.Action;

/**
 * The outcome of a task executed by an {@link Engine} to perform a given {@link Action}.
 * <p>
 * Outcomes include the task output and the set of {@link Action}s that the current user may perform next.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the task output
 */
public class TaskOutcome<T> {

	private final Collection<Action> actions;
	private final T output;

	/**
	 * Creates an instance with a set of future actions and a task output.
	 * @param actions the actions
	 * @param output the task output
	 */
	public TaskOutcome(Collection<Action> actions, T output) {
		this.actions = actions;
		this.output = output;
	}

	/**
	 * Returns the set of actions that the user may perform next.
	 * @return the actions
	 */
	public Collection<Action> nextActions() {
		return actions;
	}

	/**
	 * Returns the output of the task.
	 * @return the outpu
	 */
	public T output() {
		return output;
	}

}
