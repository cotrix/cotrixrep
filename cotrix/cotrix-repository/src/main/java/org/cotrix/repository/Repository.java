package org.cotrix.repository;

import org.cotrix.repository.query.Query;

/**
 * A repository of domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of objects in the repository
 */
public interface Repository<T> {

	/**
	 * Adds an object to this repository.
	 * @param object the object
	 */
	void add(T object);
	
	/**
	 * Returns an object in this repository.
	 * @param id the object identifier
	 * @return the object
	 */
	T lookup(String id);
	
	/**
	 * Returns the results of a {@link Query} evaluated on the objects in this repository.
	 * @param query the query
	 * @return the results
	 * 
	 * @param <R> the type of results
	 */
	<R> Iterable<R> queryFor(Query<T,R> query);
	
	/**
	 * Removes an object from this repository.
	 * @param id the object identifier
	 */
	void remove(String id);
	
	/**
	 * Updated an object in this repository.
	 * @param the set of changes to apply to the object.
	 */
	void update(T changeset);
}
