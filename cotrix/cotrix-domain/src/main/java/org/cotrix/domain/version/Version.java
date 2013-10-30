package org.cotrix.domain.version;

/**
 * An object version in an underlying versioning scheme.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Version {

	/**
	 * Returns the textual value of this version.
	 * 
	 * @return the value
	 */
	String value();

	/**
	 * Returns a new instance with a given value.
	 * 
	 * @param version the value of the new instance.
	 * @return the new instance
	 * 
	 * 
	 * @throws IllegalArgumentException if the value of the new instance is invalid for the versioning scheme that underlies this instance
	 * @throws IllegalStateException if the value of the new instance does not follow the value of this instance
	 *             according to the underlying versioning scheme
	 */
	Version bumpTo(String version);
}
