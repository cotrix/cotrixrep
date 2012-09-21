package org.cotrix.tabular.mining;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.code.Code;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;
import org.cotrix.domain.relation.validation.RelationContainerValidator;
import org.cotrix.tabular.model.Tabular;

/**
 * Given a space of codes in tabular format, this logic finds all the possible relationships, based on lexical matching.
 * 
 * Finding relations in tabular format is different from just finding relations between codes in general. Codes given in
 * tabular format do imply having a relationship on row level. These relations can be of any kind, 1-n, n-1, 1-1, etc.
 * Here only the relations 1-1 and 1-n are taken into account.
 * 
 * 
 * TODO I believe the algorithm should be changed:
 * 
 * First step should be that all n-n relations are constructed. Subsequently they can be split in real n-n, 1-n, n-1,
 * 1-1 relations.
 * 
 * 
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class RelationMiner {

	RelationConverter relationConverter = new RelationConverter();
	RelationContainerValidator validator = new RelationContainerValidator();

	/**
	 * This one is put on hold for the moment.
	 * 
	 * It was initially developed in order to find all 1-1 relations and 1-n relations.
	 * 
	 * This implementation is not finished yet.
	 * 
	 * At a certain point it was felt that this was not enough. It would be better to find all n-n relations and
	 * subsequently try to figure out which of these are real n-n, 1-n, n-1, 1-1.
	 * 
	 * Another issue not finished yet is the use of the codelistContainer. This one is yet not used. What needs to be
	 * done is relate the codes in the tabular with the ones from the codelistcontainer. The codelistcontainer is the
	 * output of the CodeMiner, input is also Tabular.
	 * 
	 * 
	 * 
	 * 
	 * @param tabular
	 * @param codelistContainer
	 * @return
	 */
	public RelationContainer mine(Tabular tabular, CodelistContainer codelistContainer) {
		RelationContainer r = new RelationContainer();

		// calculate the possible 1 to n relations

		calculateRelations1toNList(tabular, r);

		validator.validate(r);

		// build up the relations
		fillAll1toNRelations(tabular, r, codelistContainer);

		validator.validate(r);

		// delete when no relations were found
		deleteEmptyRelations(r);

		// some 1 to n are in reality just 1 to 1. Convert them
		convert1nTo11(r);

		return r;
	}

	/**
	 * check all the relations and check whether the 1-n are not in real 1-1. Convert them in case of 1-1
	 * 
	 * 
	 * 
	 * @param r
	 */
	private void convert1nTo11(RelationContainer r) {
		List<Relations1toN> list = r.getListOf1toNRelations();
		List<Relations1to1> oneToOneList = new ArrayList<Relations1to1>();
		List<Relations1toN> oneToNRemovals = new ArrayList<Relations1toN>();
		r.setListOf1to1Relations(oneToOneList);
		for (Relations1toN relations1toN : list) {
			List<Relation1toN> relList = relations1toN.getRelation1toNList();
			boolean oneToOne = true;
			for (Relation1toN relation1toN : relList) {
				if (relation1toN.getToCollection().size() > 1) {
					oneToOne = false;
				}

			}
			if (oneToOne) {
				Relations1to1 rel11 = relationConverter.convert(relations1toN);
				oneToOneList.add(rel11);
				oneToNRemovals.add(relations1toN);
			}
		}
		r.getListOf1toNRelations().removeAll(oneToNRemovals);

	}

	private void deleteEmptyRelations(RelationContainer r) {
		List<Relations1toN> list = r.getListOf1toNRelations();
		List<Relations1toN> zeroList = new ArrayList<Relations1toN>();
		for (Relations1toN relations1toN : list) {
			List<Relation1toN> relList = relations1toN.getRelation1toNList();
			if (relList.size() == 0) {
				zeroList.add(relations1toN);
			}
		}
		list.removeAll(zeroList);
	}

	private void fillAll1toNRelations(Tabular tabular, RelationContainer relationContainer,
			CodelistContainer codelistContainer) {

		validator.validate(relationContainer);

		int nrOfRows = tabular.getRows().size();
		for (int r = 0; r < nrOfRows; r++) {
			for (Relations1toN relations1toN : relationContainer.getListOf1toNRelations()) {
				ConceptTabular sourceConceptTabular = (ConceptTabular) relations1toN.getSourceConcept();
				ConceptTabular targetConceptTabular = (ConceptTabular) relations1toN.getTargetConcept();
				int sourceColumn = sourceConceptTabular.getColumnNumber();
				int targetColumn = targetConceptTabular.getColumnNumber();

				// it may be the first
				if (relations1toN.getRelation1toNList() == null) {
					relations1toN.setRelation1toNList(new ArrayList<Relation1toN>());
				}

				// deduct from the possible relations the columns and
				// subsequently the from and to codes
				List<Relation1toN> relation1toNList = relations1toN.getRelation1toNList();
				Code fromCode = new Code(tabular.getRows().get(r).get(sourceColumn));
				Code toCode = new Code(tabular.getRows().get(r).get(targetColumn));

				// build a new relation
				Relation1toN relation1toN = new Relation1toN();
				relation1toN.setFromCode(fromCode);

				if (relation1toNList.contains(relation1toN)) {
					// the relation might exist already. To be identified by the
					// fromCode (and the concept but that has been taken care of
					// already)
					relation1toN = relation1toNList.get(relation1toNList.indexOf(relation1toN));
				} else {
					// if this relation does not exist yet, it needs a new
					// toCollection
					relation1toN.setToCollection(new ArrayList<Code>());
					relation1toNList.add(relation1toN);
				}
				List<Code> toCollection = relation1toN.getToCollection();
				if (!toCollection.contains(toCode)) {
					// if the toCode has not been added yet, do it now
					toCollection.add(toCode);
				}
			}
		}
	}

	protected List<Relations1toN> calculateRelations1toNList(Tabular tabular, RelationContainer relationContainer) {
		int nrOfColumns = tabular.getRows().get(0).size();
		List<Relations1toN> listOf1toNRelations = new ArrayList<Relations1toN>();
		relationContainer.setListOf1toNRelations(listOf1toNRelations);

		// first all the possible relations are created
		for (int c = 0; c < nrOfColumns; c++) {
			for (int o = 0; o < nrOfColumns; o++) {
				if (c != o) {
					// it can not have a relationship with itself
					Relations1toN relations1toN = new Relations1toN();
					ConceptTabular sourceConcept = new ConceptTabular(c);
					ConceptTabular targetConcept = new ConceptTabular(o);
					relations1toN.setSourceConcept(sourceConcept);
					relations1toN.setTargetConcept(targetConcept);
					listOf1toNRelations.add(relations1toN);
				}
			}
		}
		return listOf1toNRelations;
	}

}
