package org.cotrix.domain.trait;


/**
 * A domain object that can return copies of itself.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the object, hence of its copy
 */
public interface Copyable<T> {

	/**
	 * Returns a copy of this object.
	 * 
	 * @return the copy
	 */
	T copy();
}
