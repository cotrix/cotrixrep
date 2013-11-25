package org.cotrix.repository.impl;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.repository.Filter;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Range;


/**
 * Partial implementation of {@link MultiQuery}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of query results
 */
public abstract class BaseQuery<T,R> implements MultiQuery<T,R> {

	private Range range = Range.ALL;
	private Collection<Filter<T>> filters = new ArrayList<Filter<T>>();
	
	@Override
	public Range range() {
		return range;
	}
	
	public RangeClause<T, R> from(final int from) {
		return new RangeClause<T,R>() {
			@Override
			public MultiQuery<T, R> to(int to) {
				range=new Range(from,to);
				return BaseQuery.this;
			}
		};
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
