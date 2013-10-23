package org.cotrix.action;

import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

import org.cotrix.action.impl.DefaultInstanceAction;

public enum CodelistAction implements Action {

	EDIT(action("edit")),
	VIEW(action("view")),
	LOCK(action("lock")),
	UNLOCK(action("unlock")),
	PUBLISH(action("seal"));	

	protected Action innerAction;

	private CodelistAction(Action action) {
		this.innerAction = action;
	}

	public List<String> parts() {
		return innerAction.parts();
	}

	public boolean isIn(Action... actions) {
		return innerAction.isIn(actions);
	}

	public boolean isIn(Collection<Action> actions) {
		return innerAction.isIn(actions);
	}

	public InstanceAction on(String instance) {
		return new DefaultInstanceAction(this,instance);
	}

	public Action value() {
		return innerAction;
	}
}
