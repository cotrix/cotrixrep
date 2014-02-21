package org.cotrix.neo;

import static org.cotrix.common.Utils.*;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoUtils {

	public static void remove(Node n) {
		
		notNull("node",n);
		
		for (Relationship r : n.getRelationships(Direction.OUTGOING)) {
			remove(r.getEndNode());
			r.delete();
		}
		
		n.delete();
			
	}
}
