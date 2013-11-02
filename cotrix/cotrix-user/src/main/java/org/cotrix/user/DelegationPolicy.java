package org.cotrix.user;

import org.cotrix.action.Action;

public interface DelegationPolicy {

	void validateDelegation(User source, User target, Action ... actions);
	
	void validateRevocation(User source, User target, Action ... actions);
}
