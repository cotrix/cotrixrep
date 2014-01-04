package org.cotrix.repository.spi;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Range;


public abstract class AbstractMultiQuery<T,R> implements MultiQuery<T,R>, Iterable<R> {

	private Range range = Range.ALL;
	private Criterion<R> criterion;
	private Collection<String> excludes = new ArrayList<String>();
	
	
	
	//subclasses provide iterators and we provide here true iterables
	@Override
	public Iterable<R> execute() {
		
		return new Iterable<R>() {
			
			@Override 
			public Iterator<R> iterator() {
				
				return AbstractMultiQuery.this.iterator();
				
			}
		};
		
	}
	@Override
	public Range range() {
		return range;
	}
	
	public RangeClause<T, R> from(final int from) {
		return new RangeClause<T,R>() {
			@Override
			public MultiQuery<T, R> to(int to) {
				range=new Range(from,to);
				return AbstractMultiQuery.this;
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
