package org.cotrix.domain.coderelation;

import java.util.List;

import org.cotrix.domain.conceptrelation.ConceptRelation;

/**
 * 
 * * The collection of 1 to 1 relations
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class Relations1to1 extends ConceptRelation {

	protected List<Relation1to1> relation1to1List;

	public List<Relation1to1> getRelation1to1List() {
		return relation1to1List;
	}

	public void setRelation1to1List(List<Relation1to1> relation1to1List) {
		this.relation1to1List = relation1to1List;
	}

}
