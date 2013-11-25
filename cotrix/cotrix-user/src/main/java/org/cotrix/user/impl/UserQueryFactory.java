package org.cotrix.user.impl;

import org.cotrix.action.ResourceType;
import org.cotrix.repository.Filter;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.cotrix.user.User;

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

	Filter<User> roleOn(String resource, ResourceType type);
}
