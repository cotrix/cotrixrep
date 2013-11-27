package org.cotrix.repository.user.impl;

import org.cotrix.action.ResourceType;
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

	MultiQuery<User, User> roleOn(String resource, ResourceType type);
	
	Criterion<User> byName();
}
