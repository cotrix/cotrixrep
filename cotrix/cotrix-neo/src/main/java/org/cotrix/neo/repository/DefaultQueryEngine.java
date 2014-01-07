package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(RUNTIME)
public class DefaultQueryEngine implements NeoQueryEngine {

	private static Logger log = LoggerFactory.getLogger(DefaultQueryEngine.class);
	
	@Inject
	private ExecutionEngine engine;
	
	@Override
	public ExecutionResult execute(String query) {
		return execute(query,Collections.<String,Object>emptyMap());
	}
	
	public ExecutionResult execute(String query, Map<String,Object> params) {
		
		log.info("executing Neo query {}",query);
		
		long time = System.currentTimeMillis();
		
		ExecutionResult result =  engine.execute(query,params);
		
		log.info("executed Neo query {} in {} ms",query,System.currentTimeMillis()-time);
		
		return result;
	};
}
