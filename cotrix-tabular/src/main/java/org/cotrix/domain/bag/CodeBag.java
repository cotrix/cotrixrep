package org.cotrix.domain.bag;

import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.coderelation.RelationContainer;

/**
 * A jar of codes and their relations.
 * 
 * Can be the result of the parsing of a CSV, containing one or more separate
 * codelists.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class CodeBag {

	protected CodelistContainer codelistContainer;
	protected RelationContainer relationContainer;

	public CodelistContainer getCodelistContainer() {
		return codelistContainer;
	}

	public void setCodelistContainer(CodelistContainer codelistContainer) {
		this.codelistContainer = codelistContainer;
	}

	public RelationContainer getRelationContainer() {
		return relationContainer;
	}

	public void setRelationContainer(RelationContainer relationContainer) {
		this.relationContainer = relationContainer;
	}

}
