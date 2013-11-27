package org.cotrix.repository.impl.memory;

import java.util.Comparator;

import org.cotrix.repository.Criterion;

public interface MCriterion<T> extends Criterion<T> {

	Comparator<T> comparator();
}
