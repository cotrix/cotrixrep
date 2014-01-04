package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;

import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

@Alternative @Priority(RUNTIME)
public class DefaultQueryEngine implements NeoQueryEngine {

	@Inject
	private ExecutionEngine engine;
	
	@Override
	public ExecutionResult execute(String query) {
		return engine.execute(query);
	}
	
	public ExecutionResult execute(String query, Map<String,Object> params) {
		return engine.execute(query, params);
	};
}
