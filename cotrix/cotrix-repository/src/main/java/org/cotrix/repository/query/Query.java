package org.cotrix.repository.query;


/**
 * A query over domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of domain objects
 * @param <R> the type of query results
 */
public interface Query<T,R> {

	/**
	 * Returns the {@link Range} of the query
	 * @return the query
	 */
	Range range();
	
	void setRange(Range range);
}
