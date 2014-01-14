package org.cotrix.neo;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

//we use it from state beans that are not CDI-managed
//i.e. must create nodes but cannot have the store injected.

public class NeoNodeFactory {

	//usual 'static' injection sim: notified at startup, set static field manually 
	static void startup(@Observes Startup event, GraphDatabaseService store) {
		
		NeoNodeFactory.store =store;
	}
	
	private static GraphDatabaseService store;
	
	//the actual factory method
	public static Node newnode(NodeType type) {
		return store.createNode(type);
	}
	
	
}
