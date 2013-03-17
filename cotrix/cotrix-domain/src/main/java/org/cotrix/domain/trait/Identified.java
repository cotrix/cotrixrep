package org.cotrix.domain.trait;


/**
 * A domain object with an identity.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Identified {

	/**
	 * Returns the identifier of this object.
	 * @return the identifier
	 */
	String id();
	
	/**
	 * A {@link Private} extension of {@link Identified}.
	 */
	public static interface Private<T extends Private<T>> extends Identified,org.cotrix.domain.trait.Private<T> {
		
		/**
		 * Sets the identifier of this object. 
		 * @param id the identifier
		 */
		void setId(String id);
	}
}
