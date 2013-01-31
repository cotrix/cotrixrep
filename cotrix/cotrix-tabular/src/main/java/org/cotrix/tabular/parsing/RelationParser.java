package org.cotrix.tabular.parsing;

import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.conceptrelation.Relations;
import org.cotrix.domain.tabular.Tabular;

/**
 * This class is the opposite of the RelationMiner. It will not assume any relation, but trying to build up a CodeJar
 * based on the given relations.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class RelationParser {

	/**
	 * Parses tabular data, using the relations as metadata
	 * 
	 * 
	 * 
	 * 
	 * @param tabular
	 * @param relations
	 * @return RelationContainer with the relevant relations
	 */
	public RelationContainer parse(Tabular tabular, Relations relations) {
		RelationContainer r = new RelationContainer();

		return r;
	}

}
