package org.cotrix.repository.query;


/**
 * Partial implementation of {@link Query}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of query results
 */
public abstract class BaseQuery<T,R> implements Query<T,R> {

	private final Range range;
	
	/**
	 * Creates an instance with default options.
	 */
	public BaseQuery() {
		this.range=Range.ALL;
	}
	
	/**
	 * Creates an instance with a given {@link Range}.
	 * @param range the range
	 */
	public BaseQuery(Range range) {
		this.range=range;
	}
	
	@Override
	public Range range() {
		return range;
	}
}
