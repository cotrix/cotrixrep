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
	protected ConceptRelationType conceptRelationType;

	public ConceptRelationType getConceptRelationType() {
		return conceptRelationType;
	}

	public void setConceptRelationType(ConceptRelationType conceptRelationType) {
		this.conceptRelationType = conceptRelationType;
	}

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
