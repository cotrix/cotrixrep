package org.cotrix.application;

import org.cotrix.domain.codelist.Codelist;

/**
 * Issues versions of domain objects
 * @author Fabio Simeoni
 *
 */
public interface VersioningService {

	
	VersionClause bump(Codelist object);
	
	
	static interface VersionClause {
		
		/**
		 * Sets the new version of the object.
		 * @param version the new version
		 * @return a versioned copy of the object.
		 */
		Codelist to(String version);
		
	}
}
