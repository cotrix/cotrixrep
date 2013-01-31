package org.cotrix.domain.conceptrelation;

/**
 * The relation between 2 concepts.
 * 
 * @author Erik van Ingen
 * 
 */

public class ConceptRelation {

	protected Concept sourceConcept;
	protected Concept targetConcept;

	public Concept getSourceConcept() {
		return sourceConcept;
	}

	public void setSourceConcept(Concept sourceConcept) {
		this.sourceConcept = sourceConcept;
	}

	public Concept getTargetConcept() {
		return targetConcept;
	}

	public void setTargetConcept(Concept targetConcept) {
		this.targetConcept = targetConcept;
	}

}
