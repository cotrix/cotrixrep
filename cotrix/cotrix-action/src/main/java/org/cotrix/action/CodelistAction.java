package org.cotrix.action;

import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

public enum CodelistAction implements Action {

	EDIT(action("edit")),
	LOCK(action("lock")),
	UNLOCK(action("unlock")),
	SEAL(action("seal"))
	;
	protected Action innerAction;

	private CodelistAction(Action innerAction) {
		this.innerAction = innerAction;
	}

	public List<String> parts() {
		return innerAction.parts();
	}

	public String instance() {
		return innerAction.instance();
	}

	public boolean isOnInstance() {
		return innerAction.isOnInstance();
	}

	public boolean isIn(Action... actions) {
		return innerAction.isIn(actions);
	}

	public boolean isIn(Collection<Action> actions) {
		return innerAction.isIn(actions);
	}

	public Action on(String instance) {
		return innerAction.on(instance);
	}
	
	@Override
	public Action onAny() {
		return innerAction.onAny();
	}
}
