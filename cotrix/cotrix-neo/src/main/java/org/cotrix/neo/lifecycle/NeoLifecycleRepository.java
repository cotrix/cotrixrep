package org.cotrix.neo.lifecycle;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.LifecycleRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.thoughtworks.xstream.XStream;

@Singleton @Alternative @Priority(RUNTIME)
public class NeoLifecycleRepository implements LifecycleRepository {
	
	private static XStream stream = new XStream(); 
	
	@Inject
	private GraphDatabaseService store;
	
	@Override
	public void add(Lifecycle lc) {
		
		Node node = store.createNode(LIFECYCLE);
		
		node.setProperty(name_prop,lc.name());
		node.setProperty(id_prop,lc.resourceId());
		node.setProperty(state_prop,stream.toXML(lc.state()));
	}

	@Override
	public ResumptionToken lookup(String id) {
		
		Node node = nodeFor(id);

		if (node==null)
			return null;
		
		String name = (String)node.getProperty(name_prop);
		State state = (State) stream.fromXML((String)node.getProperty(state_prop));
		
		return new ResumptionToken(name,state);
			
	}

	@Override
	public void update(Lifecycle lc) {
		
		Node node = nodeFor(lc.resourceId());

		if (node==null)
			throw new AssertionError("attempt to update transient lifecycle "+lc.resourceId());
		
		node.setProperty(state_prop,stream.toXML(lc.state()));
		
	}
	
	
	//helpers
	
	private Node nodeFor(String id) {
		
		try (
				ResourceIterator<Node> retrieved = store.findNodesByLabelAndProperty(LIFECYCLE,id_prop,id).iterator(); 
			) 
		{
			return retrieved.hasNext()? retrieved.next(): null;
		}
		
	}

}
