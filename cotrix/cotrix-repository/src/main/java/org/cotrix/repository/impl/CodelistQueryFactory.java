package org.cotrix.repository.impl;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.codelist.CodelistCoordinates;
import org.cotrix.repository.codelist.CodelistSummary;

/**
 * Factory of queries over codelists.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodelistQueryFactory {

	/**
	 * Returns a query for all the codelists.
	 * @return the query
	 */
	MultiQuery<Codelist,Codelist> allLists();
	
	
	/**
	 * Returns a query for the coordinates of all the codelists.
	 * @return the query
	 */
	MultiQuery<Codelist,CodelistCoordinates> allListCoordinates();
	
	/**
	 * Returns a query for all the codes of a given codelist.
	 * @return the query
	 */
	MultiQuery<Codelist,Code> allCodes(String codelistId);
	
	/**
	 * Returns a query for the summary of a given codelist.
	 * @param codelistId the codelist identifier
	 * @return the query
	 */
	Query<Codelist,CodelistSummary> summary(String codelistId);
	
	
}
