package org.cotrix.domain.common;

import java.util.Iterator;

public class DelegateContainer<S,T> implements Container<T> {

	private final Container<S> elements;
	
	public DelegateContainer(Container<S> elements) {
		this.elements=elements;
	}
	
	@Override
	public int size() {
		return elements.size();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new IteratorAdapter<T,S>(elements.iterator(),null);
	}
}
