package org.cotrix.domain.traits;

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
}
