package org.cotrix.repository;

public interface Repository<T> {

	void add(T entity) throws IllegalArgumentException,  //a changeset
						      IllegalStateException;     //already stored

	T lookup(String id); //may be null

	<R> R get(Query<T, R> query);

	void remove(String id) throws IllegalStateException; //unknown

	void update(T changeset) throws IllegalArgumentException, //not a changeset
									IllegalStateException; //unknown

	int size();
	
	void update(String id,UpdateAction<T> action);
}
