package org.cotrix.repository.impl.memory;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Constants.*;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.cotrix.action.ResourceType;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.spi.UserQueryFactory;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped @Alternative @Priority(DEFAULT)
public class MUserRepository extends MemoryRepository<User.State> implements UserQueryFactory {
	
	
	
	@Override
	public MultiQuery<User,User> allUsers() {
		
		return new MMultiQuery<User,User>() {
			
			public Collection<? extends User> _execute() {
				return adapt(getAll());
			}
		};
		
	}
	
	
	@Override
	public Query<User, User> userByName(final String name) {
		
		return new Query<User,User>() {
			@Override
			public User execute() {
				
				for (User.State user : getAll())
					if (user.name().equals(name))
						return user.entity();
				
				return null;
			}
		};
	}
	
	
	@Override
	public MultiQuery<User,User> roleOn(final String resource, final ResourceType type) {
		
		return new MMultiQuery<User,User>() {
			
			@Override
			public Collection<? extends User> _execute() {
				Collection<User> matches = new HashSet<User>();
				for (User.State u : getAll()) {
					User user = u.entity();
					if (!user.fingerprint().allRolesOver(resource,type).isEmpty())
						matches.add(user);
				}
				return matches;
			}
		};
		
	}
	
	@Override
	public MultiQuery<User,User> role(final Role role) {
		
		return new MMultiQuery<User,User>() {
			
			@Override
			public Collection<? extends User> _execute() {
				Collection<User> matches = new HashSet<User>();
				for (User.State u : getAll()) {
					User user = u.entity();
					if (user.is(role)) matches.add(user);
				}
				return matches;
			}
		};
		
	}
	
	@Override
	public MultiQuery<User, User> teamFor(final String codelistId) {
	
		return new MMultiQuery<User,User>() {
			
			@Override
			public Collection<? extends User> _execute() {
				
				Collection<User> matches = new HashSet<User>();
				
				for (User.State u : getAll()) {
					User user  = u.entity();
					if (!user.fingerprint().specificRolesOver(codelistId,codelists).isEmpty())
						matches.add(user);
				}
				return matches;
			}
		};
	}
	
	
	
	
	@Override
	public Criterion<User> byName() {
		
		return new MCriterion<User>() {
			
			public int compare(User o1, User o2) {
				return o1.name().compareTo(o2.name());
			};
		};
	}
}
