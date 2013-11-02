package org.cotrix.user.impl;

import static org.cotrix.user.PredefinedUsers.*;

import org.cotrix.action.Action;
import org.cotrix.user.DelegationPolicy;
import org.cotrix.user.User;

public class DefaultDelegationPolicy implements DelegationPolicy {

	@Override
	public void validateDelegation(User source, User target, Action ... actions) {
		
		for (Action action : actions)
			if (action.isTemplate() && !source.equals(cotrix)) //TODO use root role
				throw new IllegalAccessError(source.name()+" cannot delegate or revoke template "+action+", as it does not have root privileges");
			else
				if (!action.included(source.permissions()))
					throw new IllegalAccessError(source.name()+" cannot perform "+action+", hence cannot delegate it or revoke it to or from "+target.name());
		
		
	}
	
	@Override
	public void validateRevocation(User source, User target, Action... actions) {
		validateDelegation(source, target, actions);
		
	}
}
