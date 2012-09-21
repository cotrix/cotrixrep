package org.cotrix.domain.coderelation;

import java.util.List;

import org.cotrix.domain.conceptrelation.ConceptRelation;

/**
 * The collection of 1 to N relations.
 * 
 * 
 * This collection can be identified by the source concept
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Relations1toN extends ConceptRelation {

	protected List<Relation1toN> Relation1toNList;

	public List<Relation1toN> getRelation1toNList() {
		return Relation1toNList;
	}

	public void setRelation1toNList(List<Relation1toN> relation1toNList) {
		Relation1toNList = relation1toNList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Relation1toNList == null) ? 0 : Relation1toNList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		Relations1toN other = (Relations1toN) obj;
		return this.getSourceConcept().equals(other.getSourceConcept())
				&& this.getTargetConcept().equals(other.getTargetConcept());
	}

}
