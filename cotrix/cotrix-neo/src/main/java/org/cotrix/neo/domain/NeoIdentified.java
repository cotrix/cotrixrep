package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.domain.Constants.*;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Status;
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
		//changesets are never persisted|retrieved
		return null;
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
	
	

}
