package org.cotrix.repository.impl.memory;

import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;

public interface MQuery<T,S extends Identified.State,R> extends Query<T,R> {

	R execute(MemoryRepository<S> repository);
}
