package org.cotrix.domain.trait;


/**
 * A domain object that has a definition.
 * 
 * @author Fabio Simeoni
 */
public interface Defined<T extends Definition> {

	/**
	 * Returns the definition of this object.
	 * 
	 * @return the definition
	 */
	T definition();
	
}
