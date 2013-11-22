package org.cotrix.user.memory;

import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.memory.MQuery;
import org.cotrix.repository.memory.MRepository;
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
			public Iterable<User> _execute(MRepository<User,?> repository) {
				return repository.getAll();
			}
		};
		
	}
}
