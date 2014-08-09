package org.cotrix.domain.trait;

//used as upper bound to constrains beans.
//separated from bean interfaces to avoid viral parameters.
public interface BeanOf<E> {
	
	E entity();
}
