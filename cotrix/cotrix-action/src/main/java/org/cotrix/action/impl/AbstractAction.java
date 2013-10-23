package org.cotrix.action.impl;

import static java.util.Arrays.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.InstanceAction;

public abstract class AbstractAction implements Action {

	@Override
	public boolean isIn(Collection<Action> actions) {
		
		for (Action a : actions)
			if (this.isIn(a))
				return true;
		
		return false;
	}
	
	@Override
	public boolean isIn(Action... actions) {
		
		return isIn(asList(actions));
	}

	
	public InstanceAction on(String instance) {
		return new DefaultInstanceAction(this,instance);
	}
	
	protected abstract boolean isIn(Action a);
}
