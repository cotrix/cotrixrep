package org.cotrix.neo.domain.utils;

import org.neo4j.graphdb.Node;

public interface NeoBeanFactory<S> {

	S beanFrom(Node node);
	
}
