package org.cotrix.user;

import java.util.Collection;

import org.cotrix.action.Action;

public interface DelegationPolicy {

	void validate(User delegate,Collection<Action> actions);
}
