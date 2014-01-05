package org.cotrix.neo.repository;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.repository.Range;
import org.cotrix.repository.spi.AbstractMultiQuery;
import org.neo4j.cypher.javacompat.ExecutionResult;

public abstract class NeoMultiQuery<D,R> extends AbstractMultiQuery<D, R> {
	
	private final NeoQueryEngine engine;
	private final Map<String,Object> params = new HashMap<>();
	
	public NeoMultiQuery(NeoQueryEngine engine) {
		this.engine =engine;
	}
	
	protected void param(String key,Object value) {
		params.put(key, value);
	}
	
	protected ExecutionResult execute(String query) {
		
		Range range = range();
		
		if (range!=Range.ALL) {
			
			param("_skip",range().from()-1);
			param("_limit", range.to()-range.from()+1);
			
			query = query + " SKIP {_skip} LIMIT {_limit}";
			
			
		}

		return engine.execute(query,params);
	}
}
