package org.cotrix.user.memory;

import java.util.Collection;

import org.cotrix.action.ResourceType;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.memory.MFilter;
import org.cotrix.repository.memory.MQuery;
import org.cotrix.repository.memory.MRepository;
import org.cotrix.repository.query.Filter;
import org.cotrix.user.User;
import org.cotrix.user.impl.UserQueryFactory;
import org.cotrix.user.queries.UserQuery;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserQueryFactory implements UserQueryFactory {

	static abstract class UserMQuery<R> extends MQuery<User, R> implements UserQuery<R> {}
	
	@Override
	public UserQuery<User> allUsers() {
		
		return new UserMQuery<User>() {
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
