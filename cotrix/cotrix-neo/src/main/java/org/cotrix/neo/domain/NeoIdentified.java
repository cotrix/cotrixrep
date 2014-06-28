package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.cotrix.neo.NeoNodeFactory;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.Node;

public abstract class NeoIdentified implements Identified.State {
	
	private final Node node;
	
	//read/update scenario: node is fetched from store
	public NeoIdentified(Node node) {
		this.node=node;
	}
	
	//add scenario: node is created
	public NeoIdentified(NodeType type,Identified.State state) {
		
		this(newnode(type));
		
		this.id(state.id());
	}
	
	public Node node() {
		return node;
	}
	
	@Override
	public String id() {
		return (String) node.getProperty(id_prop);
	}
	
	void id(String id) {
		node.setProperty(id_prop, id);
	}

	@Override
	public Status status() {
		//persistent objects are never used as changesets
		return null;
	}
	
	public Node resolve(Named.State state, NodeType type) {
		
		//persisted already?
		if (state instanceof NeoIdentified)
			return NeoIdentified.class.cast(state).node();
		
		//is there an equivalent in cache?
		Node node = threadCache().get(state.id());
		
		//is there an equivalent in store?
		if (node==null) {
			
			node = NeoNodeFactory.node(type,state.id());
		
			
			if (node==null)
				throw new IllegalStateException("cannot form link: no node '"+state.name()+"' (id="+state.id()+") of type "+type+" in this repository");
			
			else threadCache().put(state.id(),node);
		}
		
		return node;
	}

	
	public Node softResolve(Named.State state, NodeType type) {
		
		try{
			
			return resolve(state,type);
			
		}
		catch(IllegalStateException e) {
			return null;
		}
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id() == null) ? 0 : id().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Identified.State))
			return false;
		Identified.State other = (Identified.State) obj;
		return id().equals(other.id());
	}
	
	@Override
	public String toString() {
		return super.toString()+":"+hashCode();
	}
	

}
