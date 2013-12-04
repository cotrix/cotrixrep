package org.cotrix.repository.impl;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Collection;

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
	private Collection<String> excludes = new ArrayList<String>();
	
	
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
	public MultiQuery<T, R> sort(Criterion<R> criterion) {
		this.criterion=criterion;
		return this;
	}
	

	public MultiQuery<T,R> excluding(String ... excludes) {
		
		this.excludes.addAll(asList(excludes));
		return this;
	};
	
	protected Criterion<R> criterion() {
		return criterion;
	}
	
	
	protected Collection<String> excludes() {
		return excludes;
	}
	
}
