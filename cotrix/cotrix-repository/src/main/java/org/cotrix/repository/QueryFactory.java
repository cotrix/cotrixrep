package org.cotrix.repository;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;

/**
 * Returns implementation of {@link Query}s for given {@link Repository}s.
 * 
 * @author Fabio Simeoni
 * 
 * @see Query
 * @see Repository
 *
 */
public interface QueryFactory {

	/**
	 * Returns a query for all the codelists in the repository.
	 * @return the query
	 */
	CodelistQuery<Codelist> allLists();
	
	
	/**
	 * Returns a query for the coordinates of all the codelists in the repository
	 * @return the query
	 */
	CodelistQuery<CodelistCoordinates> allListCoordinates();
	
	/**
	 * Returns a query for all {@link Code}s in a given {@link Codelist} in a {@link Repository}.
	 * @return the query
	 */
	CodelistQuery<Code> allCodes(String codelistId);
	
	
	Specification<Codelist,CodelistSummary> summary(String codelistId);
	
	
}
