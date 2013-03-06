package org.cotrix.domain.trait;

import org.cotrix.domain.spi.IdGenerator;

/**
 * An object that can be versioned, i.e. can yields copies with different versions.
 * <p>
 * Copying is recursive over the object's dependencies and requires an {@link IdGenerator} for the generation of new
 * identifiers.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the object, hence of its (versioned) copies
 */
public interface Versioned<T> {

	/**
	 * Returns the current version of this object.
	 * 
	 * @return the version
	 */
	String version();

	/**
	 * Returns a copy of this object with a new version.
	 * 
	 * @param generator an {@link IdGenerator}
	 * @param version the new version
	 * @return the copy
	 * @throws IllegalArgumentException if the version is syntactically invalid
	 * @throws IllegalStateException if the version is syntactically valid but the object cannot acquire it
	 */
	T bump(IdGenerator generator, String version) throws IllegalArgumentException, IllegalStateException;
}
