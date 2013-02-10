package org.cotrix.domain.traits;


/**
 * A {@link Copyable} that can be versioned, i.e. can yields copies with different versions.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the version type
 */
public interface Versioned<T extends Versioned<T>> extends Copyable<T> {

	/**
	 * Returns the current version of this object.
	 * @return the version
	 */
	String version();
	
	/**
	 * Returns a copy of this object with a new version.

	 * @param version the new version
	 * @return the copy
	 * @throws IllegalArgumentException if the version is syntactically invalid
	 * @throws IllegalStateException if the version is syntactically valid but the object cannot acquire it 
	 */
	T copyWithVersion(String version) throws IllegalArgumentException, IllegalStateException;
}
