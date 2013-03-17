package org.cotrix.repository.memory;

import org.cotrix.repository.query.BaseQuery;
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
	 * Returns a result from a given object.
	 * @param object the object
	 * @return the result, or <code>null</code> if the object does not match the query.
	 */
	public abstract R yieldFor(T object);
	
}
