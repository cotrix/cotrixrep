package org.cotrix.repository.spi;

import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;

/**
 * Returns implementation of {@link MultiQuery}s for given {@link Repository}s.
 * 
 * @author Fabio Simeoni
 * 
 * @see MultiQuery
 * @see Repository
 * 
 */
public interface UserQueryFactory {

	MultiQuery<User, User> allUsers();

	Query<User, User> userByName(String name);

	MultiQuery<User,User> teamFor(String codelistId);
	
	Criterion<User> byName();
	
	Criterion<User> byFullName();
	
	<T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2);
	
	<T> Criterion<T> descending(Criterion<T> c);

	MultiQuery<User, User> usersWithRole(Role role);
}
