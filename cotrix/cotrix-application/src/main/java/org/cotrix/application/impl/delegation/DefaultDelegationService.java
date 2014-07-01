package org.cotrix.application.impl.delegation;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.application.PermissionDelegationService;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;

public class DefaultDelegationService implements PermissionDelegationService {

	private final User currentUser;
	
	private final DelegationPolicy policy;
	
	private final UserRepository repository;
	
	@Inject
	public DefaultDelegationService(@Current User user, UserRepository repository, DelegationPolicy policy) {
		
		notNull("current user", user);
		notNull("user repository", repository);
		notNull("delegation policy",policy);
		
		this.currentUser=user;
		this.repository = repository;
		this.policy=policy;
	}
	
	@Override
	public DelegateClause delegate(final Action... actions) {
		
		return new DelegateClause() {
			
			@Override
			public void to(User user) {

				validate(user);
				
				policy.validateDelegation(currentUser, user, actions);

				User changeset = modifyUser(user).can(actions).build();
				
				repository.update(changeset);
				
			}
		};
		
	}
	
	@Override
	public DelegateClause delegate(Collection<Action> actions) {
		
		
		return delegate(actions.toArray(new Action[0]));
	}
	
	
	@Override
	public DelegateClause delegate(final Role... roles) {
		
		return new DelegateClause() {
			
			@Override
			public void to(User user) {

				validate(user);
				
				policy.validateDelegation(currentUser, user, roles);
				
				User changeset = modifyUser(user).is(roles).build();
				
				repository.update(changeset);
				
			}
		};
	}
	
	
	@Override
	public RevokeClause revoke(final Action... actions) {
		
		return new RevokeClause() {
			
			@Override
			public void from(User user) {
	
				validate(user);
				
				for (Action p : actions)
					if (!user.permissions().contains(p))
						throw new IllegalStateException(user.name()+" does not have permission "+p);
				
				policy.validateRevocation(currentUser, user, actions);
				
				User changeset = modifyUser(user).cannot(actions).build();
				
				repository.update(changeset);
				
			}
		};
	}
	
	@Override
	public RevokeClause revoke(final Role... roles) {
		
		return new RevokeClause() {
			
			@Override
			public void from(User user) {
				
				validate(user);
	
				policy.validateRevocation(currentUser, user, roles);
				
				Collection<Role> parents = new HashSet<>();
				
				for (Role role : roles)
					parents.addAll(role.directRoles());
				
				User changeset = modifyUser(user).isNoLonger(roles).is(parents).build();
				
				if (changeset.directRoles().isEmpty())
					repository.remove(user.id());
				else
					repository.update(changeset);
			}
		};
		
	}
	
	
	@Override
	public RevokeClause revoke(Collection<Action> actions) {
		return revoke(actions.toArray(new Action[0]));
	}

	
	//helper
	
	
	private void validate(User u) {
		
		if (u.id()==null)
			throw new IllegalArgumentException("cannot delegate roles to unidentified user "+u);
	}
	
	Action[] toArray(Collection<Action> actions) {
		return actions.toArray(new Action[0]);
	}
}
