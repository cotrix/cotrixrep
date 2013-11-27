package org.cotrix.repository.impl;

import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Range;


/**
 * Partial implementation of {@link MultiQuery}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of query results
 */
public abstract class BaseMultiQuery<T,R> implements MultiQuery<T,R> {

	private Range range = Range.ALL;
	private Criterion<R> criterion;
	
	@Override
	public Range range() {
		return range;
	}
	
	public RangeClause<T, R> from(final int from) {
		return new RangeClause<T,R>() {
			@Override
			public MultiQuery<T, R> to(int to) {
				range=new Range(from,to);
				return BaseMultiQuery.this;
			}
		};
	}
	
	@Override
	public Criterion<R> criterion() {
		return criterion;
	}
	
	@Override
	public void sort(Criterion<R> criterion) {
		this.criterion=criterion;
	}
	
}
