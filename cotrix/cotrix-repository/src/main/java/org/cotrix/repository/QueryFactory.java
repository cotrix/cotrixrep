package org.cotrix.repository;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Query;

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
	 * Returns a query for all {@link Codelist}s in a {@link Repository}.
	 * @return the query
	 */
	CodelistQuery<Codelist> allLists();
	
	/**
	 * Returns a query for all {@link Code}s in a given {@link Codelist} in a {@link Repository}.
	 * @return the query
	 */
	CodelistQuery<Code> allCodes(String codelistId);
	
	/**
	 * Returns a query for all {@link Codelist}s in a {@link Repository}.
	 * @return the query
	 */
	Query<Codebag,Codebag> allBags();
	
}
