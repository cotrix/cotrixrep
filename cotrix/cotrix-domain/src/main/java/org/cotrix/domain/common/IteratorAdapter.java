package org.cotrix.domain.common;

import java.util.Iterator;

import org.cotrix.domain.trait.EntityProvider;

public class IteratorAdapter<T,S extends EntityProvider<T>> implements Iterator<T> {
	
	Iterator<S> inner;
	
	public IteratorAdapter(Iterator<S> iterator) {
		this.inner=iterator;
	}
	
	
	public boolean hasNext() {
		return inner.hasNext();
	}
	
	public T next() {
		return inner.next().entity();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
