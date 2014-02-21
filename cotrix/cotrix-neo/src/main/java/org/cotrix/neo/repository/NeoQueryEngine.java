package org.cotrix.neo.repository;

import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionResult;

public interface NeoQueryEngine {

	ExecutionResult execute(String query);
	
	ExecutionResult execute(String query,Map<String,Object> params);
}
