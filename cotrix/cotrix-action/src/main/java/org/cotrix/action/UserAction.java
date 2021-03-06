package org.cotrix.action;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

public enum UserAction implements Action {

	VIEW("view"),
	EDIT("edit"),
	CHANGE_PASSWORD("change password");	
	
	protected Action inner;

	private UserAction(String part,String ... parts) {
	  this.inner = (Action) action(users,part,parts);
	}
	
	@Override
	public ResourceType type() {
		return users;
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
