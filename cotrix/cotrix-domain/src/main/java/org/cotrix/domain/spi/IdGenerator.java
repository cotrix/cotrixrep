package org.cotrix.domain.spi;


/**
 * Generates new object identifiers.
 * 
 * @author Fabio Simeoni
 * @see Factory
 */
public interface IdGenerator {

	/**
	 * Generates and returns an object identifier.
	 * @return the identifier
	 */
	String id();
}
