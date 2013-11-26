package org.cotrix.repository;

import java.util.Collection;


/**
 * A query over domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of domain objects
 * @param <R> the type of query results
 */
public interface MultiQuery<T,R> extends Query<T,Collection<R>> {

	interface RangeClause<T,R> {
		
		MultiQuery<T,R> to(int to);
	}
	
	/**
	 * Returns the {@link Range} of the query
	 * @return the query
	 */
	Range range();
	
	RangeClause<T,R> from(int from);
	
	Collection<? extends Filter<T>> filters();
	
	MultiQuery<T,R> with(Filter<T> filter);
}
