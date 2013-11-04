package org.cotrix.user.impl;

import java.util.Collection;

import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.user.DelegationPolicy;
import org.cotrix.user.PermissionDelegationService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.Users;

public class DefaultPermissionDelegationService implements PermissionDelegationService {

	@Inject @Current
	User currentUser;
	
	@Inject
	DelegationPolicy policy;
	
	@Inject
	UserRepository repository;
	
	@Override
	public DelegatePermissionClause delegate(final Action... actions) {
		
		return new DelegatePermissionClause() {
			
			@Override
			public void to(User u) {
		
				policy.validateDelegation(currentUser, u, actions);
				
				User changeset = Users.user(u).can(actions).build();
				
				repository.update(changeset);
				
			}
		};
		
	}
	
	@Override
	public DelegatePermissionClause delegate(Collection<Action> actions) {
		
		
		return delegate(actions.toArray(new Action[0]));
	}
	
	@Override
	public RevokePermissionClause revoke(final Action... actions) {
		
		return new RevokePermissionClause() {
			
			@Override
			public void from(User u) {
	
				for (Action p : actions)
					if (!u.permissions().contains(p))
						throw new IllegalStateException(u.name()+" does not have permission "+p);
				
				policy.validateDelegation(currentUser, u, actions);
				
				User changeset = Users.user(u).cannot(actions).build();
				
				repository.update(changeset);
				
			}
		};
	}
	
	@Override
	public RevokePermissionClause revoke(Collection<Action> actions) {
		return revoke(actions.toArray(new Action[0]));
	}

}
