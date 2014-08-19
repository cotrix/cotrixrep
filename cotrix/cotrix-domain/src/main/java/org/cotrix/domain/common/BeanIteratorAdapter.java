package org.cotrix.domain.common;

import java.util.Iterator;

import org.cotrix.domain.trait.BeanOf;

public class BeanIteratorAdapter<T,B extends BeanOf<? extends T>> implements Iterator<T> {
	
	Iterator<B> inner;
	
	public BeanIteratorAdapter(Iterator<B> iterator) {
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
