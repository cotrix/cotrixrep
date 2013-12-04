package org.cotrix.repository.impl.memory;

import org.cotrix.repository.Criterion;

public abstract class MQueryFactory {

	
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {

		return MCriteria.all(c1, c2);
	}

	public <T> Criterion<T> descending(final Criterion<T> c) {
		
		return MCriteria.descending(c);
	}
}
