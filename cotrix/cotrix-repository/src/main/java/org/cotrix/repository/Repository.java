package org.cotrix.repository;

/**
 * A repository of domain objects.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of objects in the repository
 */
public interface Repository<T> {

	/**
	 * Adds an entity to this repository.
	 * 
	 * @param entity the entity
	 * 
	 * @throws IllegalArgumentException if the object is a changeset or is already in this repository
	 * 
	 */
	void add(T object);

	/**
	 * Returns an entity in this repository.
	 * 
	 * @param id the entity identifier
	 * @return the entity, or <code>null</code> if no entity with the given identifier is in this repository
	 */
	T lookup(String id);

	/**
	 * Returns the results of a query evaluated over the entities in this repository.
	 * 
	 * @param query the query
	 * @param <R> the type of query results
	 * 
	 * @return the results
	 */
	<R> R get(Query<T, R> query);

	/**
	 * Removes an entity from this repository.
	 * 
	 * @param id the entity identifier
	 * 
	 * @throws IllegalStateException if no entity with the given identifier is this repository
	 */
	void remove(String id);

	/**
	 * Updates an entity in this repository.
	 * 
	 * @param the set of changes to apply to the entity
	 * 
	 * @throws IllegalArgumentException if the input is not a changeset
	 * @throws IllegalStateException if the changeset refers to an entity not in this repository
	 */
	void update(T changeset);

	/**
	 * Returns the number of entities in this repository.
	 * 
	 * @return the number of entities in this repository
	 * 
	 */
	int size();
	
	
	/**
	 * Performs a given action to update a given entity in this repository .
	 * @param action the action
	 */
	void update(String id,UpdateAction<T> action);
}
