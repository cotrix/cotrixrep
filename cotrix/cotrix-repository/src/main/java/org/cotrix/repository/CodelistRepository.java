package org.cotrix.repository;

import org.cotrix.domain.Codelist;


/**
 * 
 * A {@link Repository} of {@link Codelist}s
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodelistRepository extends Repository<Codelist> {
	
	/**
	 * Returns a summary of a given codelist
	 * @return the codelist identifier
	 * 
	 * @throws IllegalStateException if there is no codelist with the given identifier
	 */
	CodelistSummary summary(String id);
}
