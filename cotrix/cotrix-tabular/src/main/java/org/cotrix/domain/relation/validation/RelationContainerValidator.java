package org.cotrix.domain.relation.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.core.CotrixException;
import org.cotrix.domain.code.Code;
import org.cotrix.domain.coderelation.Relation1to1;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;
import org.cotrix.domain.conceptrelation.Concept;

/**
 * Having all the validations for the relationsContainer in 1 place.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class RelationContainerValidator {

	public void validate(RelationContainer relationContainer) {
		check4Concepts(relationContainer);
		check4Duplicates(relationContainer);

	}

	private void check4Duplicates(RelationContainer relationContainer) {
		for (Relations1toN relations1toN : relationContainer.getListOf1toNRelations()) {
			List<Relation1toN> list = relations1toN.getRelation1toNList();
			if (list != null) {
				Set<String> froms = new HashSet<String>();
				for (Relation1toN relation1toN : list) {
					validateCode(relation1toN.getFromCode());
					froms.add(relation1toN.getFromCode().getValue());
					Set<String> tos = new HashSet<String>();
					List<Code> codes = relation1toN.getToCollection();
					for (Code code : codes) {
						validateCode(code);
						tos.add(code.getValue());
					}
					if (tos.size() != codes.size()) {
						throw new CotrixException("Duplicate target codes detected in the relation ships, list has "
								+ tos.size() + " unique elements, full size is " + codes.size());
					}
				}
				if (list.size() != froms.size()) {
					throw new CotrixException("Duplicate source codes detected in the relation ships, list has "
							+ froms.size() + " unique elements, full size is " + list.size());

				}
			}
		}
		if (relationContainer.getListOf1to1Relations() != null) {

			for (Relations1to1 relations1to1 : relationContainer.getListOf1to1Relations()) {
				List<Relation1to1> list = relations1to1.getRelation1to1List();
				Set<String> froms = new HashSet<String>();
				Set<String> tos = new HashSet<String>();

				for (Relation1to1 relation1to1 : list) {
					froms.add(relation1to1.getSourceCode().getValue());
					tos.add(relation1to1.getTargetCode().getValue());
				}
				if (list.size() == froms.size()) {
					throw new CotrixException("Duplicate source codes detected in the relation ships ");
				}
				if (list.size() == tos.size()) {
					throw new CotrixException("Duplicate target codes detected in the relation ships ");
				}
			}
		}

	}

	private void check4Concepts(RelationContainer relationContainer) {
		for (Relations1toN relations1toN : relationContainer.getListOf1toNRelations()) {
			if (isEmpty(relations1toN.getSourceConcept())) {
				throw new CotrixException("Source concept of the 1-N relation is not defined");
			}
			if (isEmpty(relations1toN.getTargetConcept())) {
				throw new CotrixException("Target concept of the 1-N relation is not defined");
			}
			if (relations1toN.getSourceConcept().getValue().equals(relations1toN.getTargetConcept().getValue())) {
				throw new CotrixException("Source and target concept can never be the same in a relation");
			}
		}
	}

	private void validateCode(Code code) {
		if (code == null || isEmpty(code.getValue())) {
			throw new CotrixException("Code is empty");
		}
	}

	private boolean isEmpty(Concept concept) {
		return concept == null || isEmpty(concept.getValue());
	}

	boolean isEmpty(String space) {
		return (space == null || space.equals(""));
	}
}
