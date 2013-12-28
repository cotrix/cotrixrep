package org.cotrix.neo.domain.utils;

import java.util.Iterator;

import org.cotrix.domain.trait.Identified;
import org.neo4j.graphdb.Relationship;

public class NeoIterator<S extends Identified.State> implements Iterator<S> {

	private final Iterator<Relationship> inner;
	private final NeoFactory<S> factory;
	
	public NeoIterator(Iterator<Relationship> inner,NeoFactory<S> provider) {
		this.inner = inner;
		this.factory=provider;
	}
	
	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}

	@Override
	public S next() {
		return factory.beanFrom(inner.next().getEndNode());
	}

	@Override
	public void remove() {
		inner.remove();
		
	}

	
}
