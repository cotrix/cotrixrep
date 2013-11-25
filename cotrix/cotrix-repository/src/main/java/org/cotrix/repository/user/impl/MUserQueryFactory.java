package org.cotrix.repository.user.impl;

import java.util.Collection;

import org.cotrix.action.ResourceType;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Filter;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.codelist.impl.CodelistQueryFactory;
import org.cotrix.repository.impl.memory.MFilter;
import org.cotrix.repository.impl.memory.MMultiQuery;
import org.cotrix.repository.impl.memory.MQuery;
import org.cotrix.repository.impl.memory.MemoryRepository;

/**
 * A {@link CodelistQueryFactory} for {@link MMultiQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserQueryFactory implements UserQueryFactory {
 
	@Override
	public MultiQuery<User,User> allUsers() {
		
		return new MMultiQuery<User,User>() {
			public Collection<? extends User> executeOn(MemoryRepository<? extends User> repository) {
				return repository.getAll();
			}
		};
		
	}
	
	
	@Override
	public Query<User, User> userByName(final String name) {
		return new MQuery<User,User>() {
			@Override
			public User execute(MemoryRepository<? extends User> repository) {
				
				for (User user : repository.getAll())
					if (user.name().equals(name))
						return user;
				
				return null;
			}
		};
	}
	
	
	@Override
	public Filter<User> roleOn(final String resource, final ResourceType type) {
		
		return new MFilter<User>() {
			
			@Override
			public boolean matches(User user) {
				try {
					return !user.fingerprint().rolesOver(resource,type).isEmpty();
				}
				catch(NullPointerException e) {
					return false;
				}
			}
		};
		
	}
}
