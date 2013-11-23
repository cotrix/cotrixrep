package org.cotrix.user.memory;

import java.util.Collection;

import org.cotrix.action.ResourceType;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.memory.MFilter;
import org.cotrix.repository.memory.MQuery;
import org.cotrix.repository.memory.MRepository;
import org.cotrix.repository.query.Filter;
import org.cotrix.repository.query.Query;
import org.cotrix.user.User;
import org.cotrix.user.impl.UserQueryFactory;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserQueryFactory implements UserQueryFactory {

	@Override
	public Query<User, User> allUsers() {
		
		return new MQuery<User,User>() {
			public Collection<User> executeOn(MRepository<User,?> repository) {
				return repository.getAll();
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
