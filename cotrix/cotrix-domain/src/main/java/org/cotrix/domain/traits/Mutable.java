package org.cotrix.domain.traits;

/**
 * A domain object that can change over time.
 * <p>
 * A mutable object can update itself with the contents of an equally typed object, the <em>delta object</em>, provided
 * that the contents of the delta objects are explicitly marked as changes to the mutable object.
 * 
 * @author Fabio Simeoni
 * 
 * <T> the type of the mutable object
 */
public interface Mutable<T> {

	/**
	 * Updates this object with a given delta object.
	 * 
	 * @param delta the delta object
	 * @throws IllegalArgumentException if the delta object is malformed
	 * @throws IllegalStateException if the object cannot be updated with the delta object
	 */
	void update(T delta) throws IllegalArgumentException, IllegalStateException;
}
