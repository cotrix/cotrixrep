package org.cotrix.user.impl;

import org.cotrix.action.ResourceType;
import org.cotrix.repository.Repository;
import org.cotrix.repository.query.Filter;
import org.cotrix.repository.query.Query;
import org.cotrix.user.User;
import org.cotrix.user.queries.UserQuery;

/**
 * Returns implementation of {@link Query}s for given {@link Repository}s.
 * 
 * @author Fabio Simeoni
 * 
 * @see Query
 * @see Repository
 *
 */
public interface UserQueryFactory {

	UserQuery<User> allUsers();
	
	
	Filter<User> roleOn(String resource, ResourceType type);
}
