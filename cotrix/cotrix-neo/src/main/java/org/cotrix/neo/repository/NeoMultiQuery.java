package org.cotrix.neo.repository;

import org.cotrix.repository.spi.AbstractMultiQuery;
import org.neo4j.cypher.javacompat.ExecutionResult;

public abstract class NeoMultiQuery<D,R> extends AbstractMultiQuery<D, R> {
	
	protected ExecutionResult execute(NeoQueryEngine engine,String query) {
		
		//TODO apply configuration
		
		return engine.execute(query);
	}
}
