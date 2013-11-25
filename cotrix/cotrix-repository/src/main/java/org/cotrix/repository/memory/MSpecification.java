package org.cotrix.repository.memory;

import org.cotrix.repository.Specification;


public interface MSpecification<T,R> extends Specification<T,R> {

	R execute(MemoryRepository<? extends T> repository);
	
}
