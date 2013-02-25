package org.cotrix.domain.utils;

import org.cotrix.domain.spi.Factory;

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
	String generateId();
}
