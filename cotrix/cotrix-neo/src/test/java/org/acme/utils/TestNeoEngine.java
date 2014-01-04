package org.acme.utils;

import static org.cotrix.common.Constants.*;

import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.neo.repository.NeoQueryEngine;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(TEST)
public class TestNeoEngine implements NeoQueryEngine {

	private static Logger log = LoggerFactory.getLogger(TestNeoEngine.class);
	
	@Inject
	GraphDatabaseService store;
	
	public TestNeoEngine() {
		log.info("creating Neo query engine for testing");
	}
	
	@Override
	public ExecutionResult execute(String query) {
		
		return new ExecutionEngine(store).execute(query);
	}
	
	@Override
	public ExecutionResult execute(String query, Map<String, Object> params) {
		
		return new ExecutionEngine(store).execute(query,params);
	}
}
