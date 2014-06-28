package org.cotrix.neo;

import static java.lang.System.*;
import static org.cotrix.neo.domain.Constants.*;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//we use it from state beans that are not CDI-managed
//i.e. must create nodes but cannot have the store injected.

public class NeoNodeFactory {
	
	private static final Logger log = LoggerFactory.getLogger(NeoNodeFactory.class);

	//usual 'static' injection sim: notified at startup, set static field manually 
	static void startup(@Observes Startup event, GraphDatabaseService store) {
		
		NeoNodeFactory.store =store;
	}
	
	private static GraphDatabaseService store;
	
	//the actual factory method
	public static Node newnode(NodeType type) {
		return store.createNode(type);
	}
	
	public static Node node(NodeType type, String id) {
		
		Node node = nodeFor(type,id);
		
		return node == null ? null :node;
	}
	
	private static Node nodeFor(NodeType type, String id) {
		
		long time = currentTimeMillis();
		try (
				
			ResourceIterator<Node> retrieved = store.findNodesByLabelAndProperty(type,id_prop,id).iterator(); 
		) 
		{
			log.trace("looked up node in {} ms",currentTimeMillis()-time);
			return retrieved.hasNext()? retrieved.next(): null;
		}
		
	}
	
}
