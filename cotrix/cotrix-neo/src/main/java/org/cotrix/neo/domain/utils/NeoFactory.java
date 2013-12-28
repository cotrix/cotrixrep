package org.cotrix.neo.domain.utils;

import org.cotrix.domain.trait.Identified;
import org.neo4j.graphdb.Node;

public interface NeoFactory<S extends Identified.State> {

	S beanFrom(Node node);
	
	Node nodeFrom(S state);
}
