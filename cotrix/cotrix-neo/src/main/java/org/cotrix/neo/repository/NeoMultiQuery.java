package org.cotrix.neo.repository;

import static java.lang.String.*;
import static org.cotrix.common.CommonUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cotrix.neo.domain.Constants;
import org.cotrix.repository.Range;
import org.cotrix.repository.spi.AbstractMultiQuery;
import org.neo4j.cypher.javacompat.ExecutionResult;

public abstract class NeoMultiQuery<D,R> extends AbstractMultiQuery<D, R> {
	
	private final NeoQueryEngine engine;
	private final Map<String,Object> params = new HashMap<>();
	
	private final List<String> matches = new ArrayList<>();
	private final List<String> withs = new ArrayList<>();
	private final List<String> wheres = new ArrayList<>();
	private final List<String> orderBys = new ArrayList<>();
	private final List<String> returns = new ArrayList<>();
	
	
	private String query = null;
	
	private boolean descending =false;
	
	public NeoMultiQuery(NeoQueryEngine engine) {
		this.engine =engine;
	}
	
	public void setDescending() {
		this.descending = true;
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
	
	protected void where(String clause) {
		wheres.add(clause);
	}
	
	protected void rtrn(String clause) {
		returns.add(clause);
	}
	
	protected void param(String key,Object value) {
		params.put(key, value);
	}
	
	protected synchronized ExecutionResult executeNeo() {
		
		if (query==null)
			this.query = query();

				
		return engine.execute(query,params);
	}
	
	
	private String query() {
		
		//  match where? with? return order-by? range?
		String template = "MATCH %1$s %2$s %3$s RETURN %4$s %5$s %6$s";
		
		String orderBy = "";
		
		if (criterion()!=null) {
			NeoCriterion<?> criterion = reveal(criterion(),NeoCriterion.class);
			String response = criterion.process(this);
			if (!response.isEmpty())
				orderBy = "ORDER BY "+response;
		}
		
			
		
		String match = flatten(matches);
		
		for (String exclude : excludes())
			where(format("%1$s.%2$s <> '%3$s'",NeoCodelistQueries.$node,Constants.id_prop,exclude));
		
		String where = excludes().isEmpty()?"":"WHERE "+flatten(wheres);
		
		String with = withs.isEmpty()?"":"WITH "+flatten(withs);
		String rtrn = flatten(returns);
	
		String range = "";
		
		if (range()!=Range.ALL) {
			
			param("_skip",range().from()-1);
			param("_limit", range().to()-range().from()+1);
			
			range = " SKIP {_skip} LIMIT {_limit}";
			
			
		}
		
		String query = format(template,match,where,with,rtrn,orderBy,range); 
		
		if (descending)
			query = query.replaceAll("\\bORDER BY\\s*(\\S+)", "ORDER BY $1 DESC");
		
		return query;
	}
	

	
	//helpers
	
	private static String flatten(List<String> clauses) {
		
		if (clauses.isEmpty())
			return "";
					
		StringBuilder b = new StringBuilder();
		
		Iterator<String> it = clauses.iterator();
		while (it.hasNext()) 
			b.append(it.next()).append(it.hasNext()?", ":"");

		return b.toString();
	}
}
