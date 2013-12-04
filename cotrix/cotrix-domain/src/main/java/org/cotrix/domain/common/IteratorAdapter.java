package org.cotrix.domain.common;

import java.util.Iterator;

import org.cotrix.domain.common.Container.Provider;

public class IteratorAdapter<T,S> implements Iterator<T> {
	
	Iterator<S> inner;
	Provider<T,S> provider;
	
	public IteratorAdapter(Iterator<S> iterator,Provider<T,S> provider) {
		this.inner=iterator;
		this.provider=provider;
	}
	
	
	public boolean hasNext() {
		return inner.hasNext();
	}
	
	public T next() {
		return provider.objectFor(inner.next());
	}
	
	public void remove() {
		inner.remove();
	}
}
