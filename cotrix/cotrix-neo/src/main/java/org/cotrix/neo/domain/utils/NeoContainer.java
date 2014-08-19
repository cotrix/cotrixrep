package org.cotrix.neo.domain.utils;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.neo.domain.Constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Named;
import org.cotrix.neo.NeoUtils;
import org.cotrix.neo.domain.Constants.Relations;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


//besides additions, each operation performs a lazy traversal
//there is no local indexing to optimise chatty session.
//event at large cardinalities, we're assuming nodes remain in cache for the session.

//id-based access is memory-efficient, i.e. creates no unnecessary beans. 
//name-based access is memory-inefficient, creates unnecessary beans to retain encapsulation (
//                                          some beans don't have names, derive them)


public class NeoContainer<S extends Named.Bean> implements Container.Bean<S>  {
 
	private final Node owner;
	private final Relations relation;
	private final NeoStateFactory<S> make;
	
	public NeoContainer(Node node, Relations relation,NeoStateFactory<S> factory) {

		this.owner=node;
		this.relation=relation;
		this.make=factory;
	
	}
	
	@Override
	public Iterator<S> iterator() {
		return new NeoRelationshipIterator<>(relationships(),make);
	}

	@Override
	public int size() {
		
		return count(relationships());
	
	}
	
	@Override
	public void add(S element) {
		
		owner.createRelationshipTo(make.nodeFrom(element), relation);
		
	}

	@Override
	public void remove(String id) {
		
		for (Node n :nodes())
			
			if (id.equals(n.getProperty(id_prop))) {
				NeoUtils.removeNode(n);
				break;
			}
	}
	

	
	@Override
	public S lookup(String id) {
		
		Node node = nodeWith(id);
		
		return node == null ? null: make.beanFrom(node);
	}
	
	@Override
	public boolean contains(String id) {
		
		return nodeWith(id)!=null;
		
	}
	
	
	@Override
	public Collection<S> get(Collection<String> ids) {
		
		Collection<S> matches = new ArrayList<>();
		
		Collection<String> idCopy = new ArrayList<>(ids);
		
		for (Node node : nodes())
			
			if (idCopy.contains(idOf(node))) {
				
				matches.add(make.beanFrom(node));
				
				idCopy.remove(idOf(node));
				
				if (idCopy.isEmpty())
					break;
			}
		
				
		return matches;
	}

	@Override
	public boolean contains(QName name) {
		
		for (Node node : nodes())  {
		
			//inefficient: create and discard
			//but must retain encapsulation (e.g. attributes derive their names from definitions)
			S bean = make.beanFrom(node);
			
			if (name.equals(bean.qname()))   
				return true;
		}
		
		return false;
	}
	
	@Override
	public Collection<S> get(QName name) {
		
		Collection<S> matches = new ArrayList<>();
		
		for (Node n : nodes()) {
			
			//inefficient: create and potentially discard
			//but must retain encapsulation (e.g. attributes derive their names from definitions)
			S bean = make.beanFrom(n);
			
			if (name.equals(bean.qname()))
				matches.add(bean);
		}
		
		return matches;
	}
	
	@Override
	public S getFirst(QName name) throws IllegalStateException {
		
		for (Node n : nodes()) {
			
			//inefficient: create and potentially discard
			//but must retain encapsulation (e.g. attributes derive their names from definitions)
			S bean = make.beanFrom(n);
			
			if (name.equals(bean.qname()))
				return bean;
		}
		
		return null;
	}
	
	
	//helpers
	
	private Iterable<Relationship> relationships() {
		return owner.getRelationships(Direction.OUTGOING,relation);
	}
	
	private Iterable<Node> nodes() {
		return new Nodes(relationships());
	}
	
	private Node nodeWith(String id) {
		
		//repeat lookup idiom
		for (Node n : nodes())
			if (id.equals(idOf(n)))
				return n;
		
		return null;
	}
	
	private String idOf(Node n) {
		
		return (String) n.getProperty(id_prop,null);
			
	}
	
	//iterates over relationships' end nodes
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
