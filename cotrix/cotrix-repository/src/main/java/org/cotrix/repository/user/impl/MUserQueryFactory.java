package org.cotrix.repository.user.impl;

import java.util.Collection;
import java.util.HashSet;

import org.cotrix.action.ResourceType;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.codelist.impl.CodelistQueryFactory;
import org.cotrix.repository.impl.memory.MCriterion;
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
	public MultiQuery<User,User> roleOn(final String resource, final ResourceType type) {
		
		return new MMultiQuery<User,User>() {
			
			@Override
			public Collection<? extends User> executeOn(MemoryRepository<? extends User> repository) {
				Collection<User> matches = new HashSet<User>();
				for (User u : repository.getAll())
					if (!u.fingerprint().rolesOver(resource,type).isEmpty())
						matches.add(u);
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
