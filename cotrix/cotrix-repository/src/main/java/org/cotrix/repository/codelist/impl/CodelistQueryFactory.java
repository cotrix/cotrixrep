package org.cotrix.repository.codelist.impl;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.repository.Criterion;
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
	 * 
	 * @return the query
	 */
	MultiQuery<Codelist, Codelist> allLists();

	/**
	 * Returns a query for the coordinates of all the codelists.
	 * 
	 * @return the query
	 */
	MultiQuery<Codelist, CodelistCoordinates> allListCoordinates();

	/**
	 * Returns a query for all the codes of a given codelist.
	 * 
	 * @return the query
	 */
	MultiQuery<Codelist, Code> allCodes(String codelistId);

	/**
	 * Returns a query for the summary of a given codelist.
	 * 
	 * @param codelistId the codelist identifier
	 * @return the query
	 */
	Query<Codelist, CodelistSummary> summary(String codelistId);

	/**
	 * Returns the criterion to sort codelist results by name.
	 * 
	 * @return the criterion
	 */
	Criterion<Codelist> byCodelistName();

	/**
	 * Returns the criterion to sort codelist results by name.
	 * 
	 * @return the criterion
	 */
	Criterion<Code> byCodeName();
	
	
	/**
	 * Returns the criterion to sort codelist results by name.
	 * 
	 * @return the criterion
	 */
	Criterion<CodelistCoordinates> byCoordinateName();

	/**
	 * Returns a criterion that sorts two results according to a given criterion whenever they are equal according to
	 * yet another criterion.
	 * 
	 * @param c1 the first criterion
	 * @param c2 the second criterion
	 * 
	 * @return the combined criteria
	 */
	<T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2);
	
	/**
	 * Returns the criterion to sort codelist results by version
	 * @return the criterion
	 */
	Criterion<Codelist> byVersion();
	
	/**
	 * Returns the criterion to sort code results by attribute properties.
	 * @param attribute the attribute the describes the required properties
	 * @return  the criterion
	 */
	Criterion<Code> byAttribute(final Attribute attribute);

}
