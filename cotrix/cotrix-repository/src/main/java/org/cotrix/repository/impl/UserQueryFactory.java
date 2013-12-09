package org.cotrix.repository.impl;

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
	
	MultiQuery<User,User> teamFor(String codelistId);
	
	Criterion<User> byName();
	
	/**
	 * Returns a criterion that sorts two results according to a given criterion whenever they are equal according to
	 * yet another criterion.
	 * 
	 * @param c1 the first criterion
	 * @param c2 the second criterion
	 * 
	 * @return the combined criteria
	 */
	<T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2);
	

	/**
	 * Returns a criterion that inverts the order of a given criterion.
	 * 
	 * @param c the criterion
	 * 
	 * @return the inverted criterion
	 */
	<T> Criterion<T> descending(Criterion<T> c);
}
