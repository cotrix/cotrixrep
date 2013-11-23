package org.cotrix.repository.memory;

import org.cotrix.repository.query.Filter;

public interface MFilter<T> extends Filter<T> {

	boolean matches(T t);
}
