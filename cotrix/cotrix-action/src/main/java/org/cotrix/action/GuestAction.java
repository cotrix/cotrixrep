package org.cotrix.action;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.action.Actions.*;

import java.util.Collection;
import java.util.List;

public enum GuestAction implements Action {


	LOGIN("login"),
	SIGNUP("signup")
	;
	
	protected Action inner;

	private GuestAction(String part,String ... parts) {
		this.inner = (Action) action(application,part,parts);
	}
	
	@Override
	public ResourceType type() {
		return application;
	}

	public List<String> labels() {
		return inner.labels();
	}

	public Action value() {
		return inner;
	}
	
	@Override
	public boolean isTemplate() {
		return inner.isTemplate();
	}

	@Override
	public Action on(String instance) {
		return inner.on(instance);
	}

	@Override
	public String resource() {
		return inner.resource();
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
