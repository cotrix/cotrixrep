package org.cotrix.repository.impl.memory;

import org.cotrix.repository.Filter;

public interface MFilter<T> extends Filter<T> {

	boolean matches(T t);
}
