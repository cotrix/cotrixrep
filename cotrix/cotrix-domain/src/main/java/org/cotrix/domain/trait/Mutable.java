package org.cotrix.domain.trait;

/**
 * A domain object that changes over time.
 * <p>
 * Mutable objects can update themselves to reflect a set of changes carried by an equally typed object, called the <em>delta object</em>.
 * Vice versa, mutable objects can be used as delta objects for other objects of the same type.
 * 
 * @author Fabio Simeoni
 * 
 * <T> the type of the mutable object, hence of its delta objects
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
	
	/**
	 * Returns the type of incremental change represented by this object, if any.
	 * @return the type of change
	 */
	Change change();
	
	/**
	 * Returns <code>true</code> if the object represents a change.
	 * @return <code>true</code> if the object represents a change
	 */
	boolean isChangeset();
	
	/**
	 * Turns a delta object into a normal object.
	 */
	void reset();
}
