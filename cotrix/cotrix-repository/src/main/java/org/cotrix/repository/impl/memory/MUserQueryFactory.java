package org.cotrix.repository.impl.memory;

import static org.cotrix.action.ResourceType.*;

import java.util.Collection;
import java.util.HashSet;

import org.cotrix.action.ResourceType;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.impl.CodelistQueryFactory;
import org.cotrix.repository.impl.UserQueryFactory;

/**
 * A {@link CodelistQueryFactory} for {@link MMultiQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserQueryFactory extends AbstractMQueryFactory implements UserQueryFactory {
 
	@Override
	public MultiQuery<User,User> allUsers() {
		
		return new MMultiQuery<User,User.State,User>() {
			
			public Collection<? extends User> executeOn(MemoryRepository<User.State> repository) {
				return adapt(repository.getAll());
			}
		};
		
	}
	
	
	@Override
	public Query<User, User> userByName(final String name) {
		
		return new MQuery<User,User.State,User>() {
			@Override
			public User execute(MemoryRepository<User.State> repository) {
				
				for (User.State user : repository.getAll())
					if (user.name().equals(name))
						return user.entity();
				
				return null;
			}
		};
	}
	
	
	@Override
	public MultiQuery<User,User> roleOn(final String resource, final ResourceType type) {
		
		return new MMultiQuery<User,User.State,User>() {
			
			@Override
			public Collection<? extends User> executeOn(MemoryRepository<User.State> repository) {
				Collection<User> matches = new HashSet<User>();
				for (User.State u : repository.getAll()) {
					User user = u.entity();
					if (!user.fingerprint().allRolesOver(resource,type).isEmpty())
						matches.add(user);
				}
				return matches;
			}
		};
		
	}
	
	@Override
	public MultiQuery<User, User> teamFor(final String codelistId) {
	
		return new MMultiQuery<User,User.State,User>() {
			
			@Override
			public Collection<? extends User> executeOn(MemoryRepository<User.State> repository) {
				
				Collection<User> matches = new HashSet<User>();
				
				for (User.State u : repository.getAll()) {
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
