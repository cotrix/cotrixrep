package org.cotrix.repository.query;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Partial implementation of {@link Query}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of query results
 */
public abstract class BaseQuery<T,R> implements Query<T,R> {

	private Range range = Range.ALL;
	private Collection<Filter<T>> filters = new ArrayList<Filter<T>>();
	
	@Override
	public Range range() {
		return range;
	}
	
	public void setRange(Range range) {
		this.range = range;
	}
	
	@Override
	public Collection<? extends Filter<T>> filters() {
		return filters;
	}
	
	public BaseQuery<T,R> with(Filter<T> filter) {
		
		notNull("filter",filter);
		filters.add(filter);
		
		return this;
		
	};
	
}
