package org.cotrix.neo.domain.utils;

import org.cotrix.domain.trait.Identified;
import org.neo4j.graphdb.Node;

public interface NeoStateFactory<S extends Identified.Bean> extends NeoBeanFactory<S> {
	
	Node nodeFrom(S state);
}
