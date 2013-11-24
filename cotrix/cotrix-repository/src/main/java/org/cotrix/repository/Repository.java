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
	 * <p>
	 * The object acquires an identity as a side-effect.
	 * 
	 * @param object the object
	 */
	void add(T object);

	/**
	 * Returns an object in this repository.
	 * 
	 * @param id the object identifier
	 * @return the object
	 */
	T lookup(String id);

	/**
	 * Returns the results of a query evaluated over the objects in this repository.
	 * 
	 * @param query the query
	 * @param <R> the type of query results
	 * 
	 * @return the results
	 */
	// we choose an iterable for persistence bindings that stream results
	// we prefer iterables to iterators here, for their convenience and because we do not need remove semantics
	
	<R> Iterable<R> queryFor(Query<T, R> query);

	/**
	 * Removes an object from this repository.
	 * 
	 * @param id the object identifier
	 */
	void remove(String id);

	/**
	 * Updates an object in this repository.
	 * 
	 * @param the set of changes to apply to the object
	 */
	void update(T changeset);

	/**
	 * Returns the number of objects in this repository.
	 * 
	 * @return the number of objects in this repository
	 */
	int size();
}
