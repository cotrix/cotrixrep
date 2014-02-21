package org.cotrix.neo.repository;

import org.cotrix.repository.Criterion;

public abstract class NeoCriterion<R> implements Criterion<R> {

	protected abstract String process(NeoMultiQuery<?,?> query);
}
