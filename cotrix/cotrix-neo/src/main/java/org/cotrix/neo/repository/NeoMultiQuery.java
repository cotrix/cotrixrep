package org.cotrix.neo.repository;

import static java.lang.String.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cotrix.repository.Range;
import org.cotrix.repository.spi.AbstractMultiQuery;
import org.neo4j.cypher.javacompat.ExecutionResult;

public abstract class NeoMultiQuery<D,R> extends AbstractMultiQuery<D, R> {
	
	private final NeoQueryEngine engine;
	private final Map<String,Object> params = new HashMap<>();
	
	private final List<String> matches = new ArrayList<>();
	private final List<String> withs = new ArrayList<>();
	private final List<String> orderBys = new ArrayList<>();
	private final List<String> returns = new ArrayList<>();
	
	public NeoMultiQuery(NeoQueryEngine engine) {
		this.engine =engine;
	}
	
	protected void match(String clause) {
		matches.add(clause);
	}
	
	protected void with(String clause) {
		withs.add(clause);
	}
	
	protected void orderBy(String clause) {
		orderBys.add(clause);
	}
	
	protected void rtrn(String clause) {
		returns.add(clause);
	}
	
	protected void param(String key,Object value) {
		params.put(key, value);
	}
	
	protected ExecutionResult executeNeo() {
		
		//  match with? return order-by? range?
		String template = "MATCH %1$s %2$s RETURN %3$s %4$s %5$s";
		
		String orderBy = "";
		
		if (criterion()!=null) {
			NeoCriterion<?> criterion = reveal(criterion(),NeoCriterion.class);
			String response = criterion.process(this);
			if (!response.isEmpty())
				orderBy = "ORDER BY "+response;
		}
		
		String match = flatten(matches);
		String with = withs.isEmpty()?"":"WITH "+flatten(withs);
		String rtrn = flatten(returns);
	
		String range = "";
		
		if (range()!=Range.ALL) {
			
			param("_skip",range().from()-1);
			param("_limit", range().to()-range().from()+1);
			
			range = " SKIP {_skip} LIMIT {_limit}";
			
			
		}
		
		String query = format(template,match,with,rtrn,orderBy,range); 
				
		return engine.execute(query,params);
	}
	
	//helpers
	
	private static String flatten(List<String> clauses) {
		
		if (clauses.isEmpty())
			return "";
					
		StringBuilder b = new StringBuilder();
		
		Iterator<String> it = clauses.iterator();
		while (it.hasNext()) 
			b.append(it.next()).append(it.hasNext()?',':"");

		return b.toString();
	}
}
