package org.cotrix.repository.spi;


public interface StateRepository<S> {

	void add(S object);

	boolean contains(String id);
	
	S lookup(String id);

	void remove(String id);

	int size();
}
