package org.cotrix.neo.domain.utils;

import java.util.Iterator;

import org.neo4j.graphdb.Relationship;

//moved from nodes to entities during lazy iteration: iterates over entities generated from relationships' end nodes
public class NeoRelationshipIterator<S> implements Iterator<S> {

	private final Iterator<Relationship> inner;
	private final NeoBeanFactory<S> factory;
	
	public NeoRelationshipIterator(Iterable<Relationship> relationships,NeoBeanFactory<S> provider) {
		this(relationships.iterator(),provider);
	}
	
	public NeoRelationshipIterator(Iterator<Relationship> inner,NeoBeanFactory<S> provider) {
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
