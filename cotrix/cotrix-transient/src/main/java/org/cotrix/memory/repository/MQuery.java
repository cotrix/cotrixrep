package org.cotrix.memory.repository;

import org.cotrix.repository.Query;

public interface MQuery<T,R> extends Query<T,R> {

	R execute();
}