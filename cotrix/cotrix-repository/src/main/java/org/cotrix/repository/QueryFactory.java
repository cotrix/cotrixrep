package org.cotrix.repository;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
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
	 * @return the query.
	 */
	Query<Codelist,Codelist> allLists();
	
	/**
	 * Returns a query for all {@link Codelist}s in a {@link Repository}.
	 * @return the query.
	 */
	Query<Codebag,Codebag> allBags();
	
}
