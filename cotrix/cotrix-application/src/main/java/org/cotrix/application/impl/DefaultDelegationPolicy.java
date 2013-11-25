package org.cotrix.application.impl;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.domain.User;

public class DefaultDelegationPolicy implements DelegationPolicy {

	@Override
	public void validateDelegation(User source, User target, Action ... actions) {
		
		for (Action action : actions)
			if (action.isTemplate() && !source.isRoot()) //TODO use ROOT role
				throw new IllegalAccessError(source.name()+" cannot delegate or revoke template "+action+", as she does not have root privileges");
			else
				if (!source.can(action))
					throw new IllegalAccessError(source.name()+" cannot perform "+action+", hence cannot delegate it or revoke it to or from "+target.name());
		
		
	}
	
	@Override
	public void validateRevocation(User source, User target, Action... actions) {
		validateDelegation(source, target, actions);
		
	}
}
