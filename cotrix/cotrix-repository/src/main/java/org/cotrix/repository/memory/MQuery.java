package org.cotrix.repository.memory;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.query.BaseQuery;
import org.cotrix.repository.query.CodebagQuery;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Query;
import org.cotrix.repository.query.Range;

/**
 * Partial implementation of a {@link Query} evaluated against preloaded objects
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of objects
 * @param <R> the type of query results
 * 
 */
public abstract class MQuery<T,R> extends BaseQuery<T,R> {

	static abstract class CodelistMQuery<R> extends MQuery<Codelist, R> implements CodelistQuery<R> {}
	static abstract class CodebagMQuery<R> extends MQuery<Codebag, R> implements CodebagQuery<R> {}
	
	/**
	 * Creates an instance with default options.
	 */
	public MQuery() {
		super();
	}
	
	/**
	 * Creates an instance with a given {@link Range}.
	 * @param range the range
	 */
	public MQuery(Range range) {
		super(range);
	}
	
	/**
	 * Returns one or more results from a given object.
	 * @param object the object
	 * @return the results, or <code>null</code> if the object does not match the query.
	 */
	public abstract Iterable<? extends R> _execute(MRepository<T,?> repository);
	
	
	/**
	 * Returns one or more results from a given object.
	 * @param object the object
	 * @return the results, or <code>null</code> if the object does not match the query.
	 */
	public Iterable<R> execute(MRepository<T,?> repository) {
		
		Iterable<? extends R> results = _execute(repository);
		
		List<R> filtered = new ArrayList<R>();
		
		int count = 1;
		for (R r : results)
			if (count<range().from()) {
				count++;
				continue;
			}
			else {
				if (count>range().to())
					break;
				else { 
					filtered.add(r);
					count++;
				}
			}
	
		return filtered;
		
	}
	
}
