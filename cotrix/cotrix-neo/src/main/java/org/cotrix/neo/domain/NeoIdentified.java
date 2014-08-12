package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;

import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.neo.NeoNodeFactory;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.Node;

public abstract class NeoIdentified implements Identified.Bean {
	
	private final Node node;
	
	//wraps existing node (read/update)
	public NeoIdentified(Node node) {
		this.node=node;
	}
	
	//creates and wraps (add)
	public NeoIdentified(NodeType type,Identified.Bean bean) {
		
		this(newnode(type));
		
		this.id(bean.id());
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
	
	public Node resolve(Named.Bean bean, NodeType type) {
		
		//persisted already?
		if (bean instanceof NeoIdentified)
			return NeoIdentified.class.cast(bean).node();
		
		//is there an equivalent in cache?
		Node node = threadCache().get(bean.id());
		
		//is there an equivalent in store?
		if (node==null) {
			
			node = NeoNodeFactory.node(type,bean.id());
		
			if (node==null)
				throw new IllegalStateException("cannot form link: no node '"+bean.qname()+"' (id="+bean.id()+") of type "+type+" in this repository");
			
			else threadCache().put(bean.id(),node);
		}
		
		return node;
	}

	
	public Node softResolve(Named.Bean bean, NodeType type) {
		
		try{
			
			return resolve(bean,type);
			
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
		if (!(obj instanceof Identified.Bean))
			return false;
		Identified.Bean other = (Identified.Bean) obj;
		return id().equals(other.id());
	}
	
	@Override
	public String toString() {
		return super.toString()+":"+hashCode();
	}
	

}
