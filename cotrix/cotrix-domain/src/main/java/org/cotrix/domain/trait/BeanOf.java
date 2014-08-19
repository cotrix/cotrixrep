package org.cotrix.domain.trait;

//used as upper bound to constrains beans in containers.
public interface BeanOf<E> {
	
	E entity();
}
