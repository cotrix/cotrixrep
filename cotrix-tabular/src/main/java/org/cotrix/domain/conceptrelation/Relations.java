package org.cotrix.domain.conceptrelation;

import java.util.List;

/**
 * 
 * This class expresses a collection of relations which do apply to a space of
 * concepts.
 * 
 * In case of a codelist in tabular format, the different columns can have
 * relations. These relations can be expressed in this class.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Relations {

	private List<ConceptRelation> conceptRelationList;

	public List<ConceptRelation> getConceptRelationList() {
		return conceptRelationList;
	}

	public void setConceptRelationList(List<ConceptRelation> conceptRelationList) {
		this.conceptRelationList = conceptRelationList;
	}

}
