package org.cotrix.repository;



/**
 * A query over domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of domain objects
 * @param <R> the type of query results
 */
public interface MultiQuery<T,R> extends Query<T,Iterable<R>> {

	/**
	 * Returns the desired range of query results.
	 * @return the result range
	 */
	Range range();
	
	/**
	 * Sets the desired range of query results. 
	 * @param from the left bound of the range
	 * @return the clause to select the right bound of the range.
	 */
	RangeClause<T,R> from(int from);
	
	interface RangeClause<T,R> {
		
		/**
		 * Sets the right bound of the required result range.
		 * 
		 * @param to the right bound of the required result range
		 * @return this query
		 */
		MultiQuery<T,R> to(int to);
	}

	/**
	 * Sets a result sorting criterion on this query.
	 * @param criterion the criterion
	 */
	MultiQuery<T, R> sort(Criterion<R> criterion);

	/**
	 * Excludes given objects from the results, if they are included.
	 * @param excludes the results to exclude
	 */
	MultiQuery<T, R> excluding(String ... excludes);
	
}
