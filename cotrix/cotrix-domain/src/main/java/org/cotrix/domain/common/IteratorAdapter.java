package org.cotrix.domain.common;

import java.util.Iterator;

import org.cotrix.domain.trait.BeanOf;

public class IteratorAdapter<T,B extends BeanOf<? extends T>> implements Iterator<T> {
	
	Iterator<B> inner;
	
	public IteratorAdapter(Iterator<B> iterator) {
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
