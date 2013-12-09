package org.cotrix.repository.impl;

import org.cotrix.repository.Query;

public interface StateRepository<S> {

	void add(S object);

	boolean contains(String id);
	
	S lookup(String id);

	<R> R get(Query<S,R> query);

	void remove(String id);

	int size();
}
