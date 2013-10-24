package org.cotrix.action;

import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

import org.cotrix.action.impl.DefaultResourceAction;

public enum CodelistAction implements Action {

	EDIT("edit"),
	VIEW("view"),
	LOCK("lock"),
	UNLOCK("unlock"),
	PUBLISH("seal");	

	protected Action inner;

	private CodelistAction(String part,String ... parts) {
	  this.inner = action(CodelistAction.class,part,parts);
	}
	
	@Override
	public Class<? extends Action> type() {
		return inner.type();
	}

	public List<String> parts() {
		return inner.parts();
	}

	public boolean isIn(Action... actions) {
		return inner.isIn(actions);
	}

	public boolean isIn(Collection<Action> actions) {
		return inner.isIn(actions);
	}

	public ResourceAction on(String instance) {
		return new DefaultResourceAction(this,instance);
	}

	public Action value() {
		return inner;
	}
}
