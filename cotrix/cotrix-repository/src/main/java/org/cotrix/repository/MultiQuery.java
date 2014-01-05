package org.cotrix.repository;



public interface MultiQuery<D,R> extends Query<D,Iterable<R>> {

	
	RangeClause<D,R> from(int from);
	
	MultiQuery<D, R> sort(Criterion<R> criterion);

	MultiQuery<D, R> excluding(String ... excludes);

	
	interface RangeClause<D,R> {
		
		MultiQuery<D,R> to(int to);
	}

}
