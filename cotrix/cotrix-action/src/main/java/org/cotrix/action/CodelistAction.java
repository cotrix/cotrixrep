package org.cotrix.action;

import static org.cotrix.action.ActionType.*;
import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

public enum CodelistAction implements Action {

	EDIT("edit"),
	VIEW("view"),
	LOCK("lock"),
	UNLOCK("unlock"),
	SEAL("seal")
	
	;	
	
	protected Action inner;

	private CodelistAction(String part,String ... parts) {
	  this.inner = (Action) action(codelist,part,parts);
	}
	
	@Override
	public ActionType type() {
		return codelist;
	}

	public List<String> labels() {
		return inner.labels();
	}

	public Action on(String resource) {
		return inner.on(resource);
	}

	public Action value() {
		return inner;
	}

	@Override
	public String resource() {
		return inner.resource();
	}
	
	@Override
	public boolean isTemplate() {
		return inner.isTemplate();
	}
	
	@Override
	public boolean included(Action... actions) {
		return inner.included(actions);
	}

	@Override
	public boolean included(Collection<? extends Action> actions) {
		return inner.included(actions);
	}
	
	@Override
	public String toString() {
		return inner.toString();
	}
	
}
