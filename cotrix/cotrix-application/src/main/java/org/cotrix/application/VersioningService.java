package org.cotrix.application;

import org.cotrix.domain.trait.Versioned;

/**
 * Issues versions of domain objects
 * @author Fabio Simeoni
 *
 */
public interface VersioningService {

	
	/**
	 * Returns a versioned copy of a given domain object.
	 * @param object the object to version
	 * @return the clause to specify a new version
	 */
	<T extends Versioned> VersionClause<T> bump(T object);
	
	
	static interface VersionClause<T> {
		
		/**
		 * Sets the new version of the object.
		 * @param version the new version
		 * @return a versioned copy of the object.
		 */
		T to(String version);
		
	}
}
