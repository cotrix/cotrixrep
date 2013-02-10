package org.cotrix.domain.traits;

/**
 * An object that can return copies of its own self.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the object
 */
public interface Copyable<T> {

	/**
	 * Returns a copy of this object. 
	 * @return the copy
	 */
	T copy();
}
