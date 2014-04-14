package org.cotrix.neo.domain.utils;

import static org.cotrix.common.Utils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.neo4j.graphdb.Direction.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Identified;
import org.cotrix.neo.NeoUtils;
import org.cotrix.neo.domain.Constants.Relations;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoContainer<S extends Identified.State> implements NamedStateContainer<S>  {
 
	private final Node node;
	private final Relations type;
	private final NeoStateFactory<S> factory;
	
	public NeoContainer(Node node, Relations type,NeoStateFactory<S> mapper) {
		this.node=node;
		this.type=type;
		this.factory=mapper;
	}
	
	@Override
	public Iterator<S> iterator() {
		return new NeoRelationshipIterator<>(node.getRelationships(OUTGOING,type).iterator(),factory);
	}

	@Override
	public int size() {
		return count(node.getRelationships(OUTGOING, type));
	}

	@Override
	public void remove(String id) {
				
		Iterator<Relationship> it = node.getRelationships(Direction.OUTGOING,type).iterator();
		while (it.hasNext()) {
			Relationship rel = it.next();
			Node n = rel.getEndNode();
			if (id.equals(n.getProperty(id_prop))) {
				NeoUtils.removeNode(n);
				rel.delete();
				break;
			}
		}
	}

	
	@Override
	public boolean contains(Identified.State element) {
		return contains(element.id());
	}

	
	@Override
	public boolean contains(String id) {
		
		for (Node n : nodes())
			if (id.equals(n.getProperty(id_prop)))
				return true;
		
		return false;
		
	}

	@Override
	public void add(S element) {
		
		node.createRelationshipTo(factory.nodeFrom(element), type);
		
	}

	@Override
	public Collection<S> get(Collection<String> ids) {
		
		Collection<S> matches = new ArrayList<>();
		
		Collection<String> idCopy = new ArrayList<>(ids);
		
		for (Node n : nodes()) {
			
			Object nodeId = n.getProperty(id_prop);
			
			if (idCopy.contains(nodeId)) {
				
				matches.add(factory.beanFrom(n));
				
				idCopy.remove(nodeId);
				
				if (idCopy.isEmpty())
					break;
			}
		}
				
		return matches;
	}

	@Override
	public boolean contains(QName name) {
		
		for (Node n : nodes()) 
			if (name.equals(QName.valueOf((String) n.getProperty(name_prop))))
				return true;
		
		return false;
	}
	
	@Override
	public Collection<S> getAll(QName name) {
		
		Collection<S> matches = new ArrayList<>();
		
		for (Node n : nodes())
			if (name.equals(QName.valueOf((String) n.getProperty(name_prop))))
				matches.add(factory.beanFrom(n));
		
		return matches;
	}
	
	@Override
	public S lookup(QName name) throws IllegalStateException {
		
		Collection<S> matches = getAll(name);
		
		if (matches.size()==1)
			return matches.iterator().next();
		else
			throw new IllegalStateException("zero or more than one element with name "+name);
	}
	
	//helpers
	
	private Iterable<Node> nodes() {
		return new Nodes(node.getRelationships(Direction.OUTGOING,type));
	}
	
	private class Nodes implements Iterable<Node> {

		private final Iterable<Relationship> elements;
		
		public Nodes(Iterable<Relationship> elements) {
			this.elements=elements;
		}
		
		@Override
		public Iterator<Node> iterator() {
			
			final Iterator<Relationship> it = elements.iterator();
			
			return new Iterator<Node>() {

				@Override
				public boolean hasNext() {
					return it.hasNext();
				}

				@Override
				public Node next() {
					return it.next().getEndNode();
				}

				@Override
				public void remove() {
					it.next().delete();
				}
				
			};
		}
		
		
	}
}
