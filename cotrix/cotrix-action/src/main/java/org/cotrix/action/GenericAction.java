package org.cotrix.action;

import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

public enum GenericAction implements Action {

	// TODO replace with first real case
	SAMPLE(action("sample"))

	;

	protected Action innerAction;

	private GenericAction(Action innerAction) {
		this.innerAction = innerAction;
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

	public Action value() {
		return innerAction;
	}

	@Override
	public InstanceAction on(String instance) {
		return innerAction.on(instance);
	}

}
