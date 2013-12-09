package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.repository.Criterion;

public abstract class MQueryFactory {

	
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {

		return MCriteria.all(c1, c2);
	}

	public <T> Criterion<T> descending(final Criterion<T> c) {
		
		return MCriteria.descending(c);
	}
	
	public <R, SR extends EntityProvider<R>> Collection<R> adapt(Collection<SR> results) {
		Collection<R> adapted = new ArrayList<R>();
		for (SR result : results)
			adapted.add(result.entity());
		return adapted;
	}
}
