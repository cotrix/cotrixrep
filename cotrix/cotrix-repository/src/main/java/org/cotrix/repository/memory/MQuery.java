package org.cotrix.repository.memory;

import org.cotrix.repository.Query;

public interface MQuery<T,R> extends Query<T,R> {

	R execute(MemoryRepository<? extends T> repository);
}
