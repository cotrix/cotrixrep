package org.cotrix.application;

import org.cotrix.action.Action;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

public interface DelegationPolicy {

	void validateDelegation(User source, User target, Action ... actions);
	
	void validateRevocation(User source, User target, Action ... actions);
	
	void validateDelegation(User source, User target, Role ... roles);
	
	void validateRevocation(User source, User target, Role ... roles);
}
