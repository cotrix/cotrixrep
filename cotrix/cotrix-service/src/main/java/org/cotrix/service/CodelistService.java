package org.cotrix.service;

import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.conceptrelation.Relations;
import org.cotrix.tabular.model.Tabular;

public interface CodelistService {

	/**
	 * Parse tabular data into a container of codes and relations. Some metadata is needed as input, the relations.
	 * 
	 * 
	 * @param tabular
	 * @param relations
	 * @return
	 */
	public RelationContainer parse(Tabular tabular, Relations relations);

}
