package org.cotrix.action.impl;

import static java.util.Arrays.*;

import java.util.Collection;

import org.cotrix.action.Action;

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
	
	protected abstract boolean isIn(Action a);
}
