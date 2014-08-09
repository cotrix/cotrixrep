package org.cotrix.neo.domain.utils;

import java.util.Iterator;

import org.neo4j.graphdb.Node;

public class NeoNodeIterator<B> implements Iterator<B> {

	private final Iterator<Node> inner;
	private final NeoBeanFactory<B> factory;
	
	public NeoNodeIterator(Iterator<Node> inner,NeoBeanFactory<B> provider) {
		this.inner = inner;
		this.factory=provider;
	}
	
	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}

	@Override
	public B next() {
		return factory.beanFrom(inner.next());
	}

	@Override
	public void remove() {
		inner.remove();
		
	}

	
}
