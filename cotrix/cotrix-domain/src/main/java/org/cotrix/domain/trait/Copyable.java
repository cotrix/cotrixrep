package org.cotrix.domain.trait;

import org.cotrix.domain.utils.IdGenerator;

/**
 * A domain object that can return copies of itself.
 * <p>
 * Copying is recursive over the object's dependencies and requires an {@link IdGenerator} for the generation of new
 * identifiers.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the object, hence of its copy
 */
public interface Copyable<T> {

	/**
	 * Returns a copy of this object.
	 * 
	 * @param generator an {@link IdGenerator}
	 * @return the copy
	 */
	T copy(IdGenerator generator);
}
