package org.cotrix.engine;

import java.util.Collection;

import org.cotrix.action.Action;

public class TaskOutcome<T> {

	private final Collection<Action> actions;
	private final T output;
	
	public TaskOutcome(Collection<Action> actions, T output) {
		this.actions = actions;
		this.output = output;
	}
	
	public Collection<Action> nextActions() {
		return actions;
	}
	
	public T output() {
		return output;
	}
	
}
