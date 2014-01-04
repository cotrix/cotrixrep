package org.cotrix.neo.domain.utils;

import java.util.Iterator;

import org.cotrix.domain.trait.Identified;
import org.neo4j.graphdb.Node;

public class NeoNodeIterator<S extends Identified.State> implements Iterator<S> {

	private final Iterator<Node> inner;
	private final NeoFactory<S> factory;
	
	public NeoNodeIterator(Iterator<Node> inner,NeoFactory<S> provider) {
		this.inner = inner;
		this.factory=provider;
	}
	
	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}

	@Override
	public S next() {
		return factory.beanFrom(inner.next());
	}

	@Override
	public void remove() {
		inner.remove();
		
	}

	
}
