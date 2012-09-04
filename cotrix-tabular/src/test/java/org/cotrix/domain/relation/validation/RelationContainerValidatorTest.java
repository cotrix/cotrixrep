package org.cotrix.domain.relation.validation;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.core.CotrixException;
import org.cotrix.domain.code.Code;
import org.cotrix.domain.coderelation.Relation1to1;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;
import org.cotrix.domain.conceptrelation.Concept;
import org.junit.Test;

import static org.junit.Assert.fail;

public class RelationContainerValidatorTest {
	RelationContainerValidator relationContainerValidator = new RelationContainerValidator();

	@Test
	public void testValidate() {

		RelationContainer c = new RelationContainer();
		List<Relations1to1> listOf1to1Relations = new ArrayList<Relations1to1>();
		List<Relations1toN> listOf1toNRelations = new ArrayList<Relations1toN>();

		Relations1to1 relations1to1 = new Relations1to1();
		Relations1toN relations1toN = new Relations1toN();

		listOf1to1Relations.add(relations1to1);
		listOf1toNRelations.add(relations1toN);

		List<Relation1to1> relation1to1List = new ArrayList<Relation1to1>();
		List<Relation1toN> relation1toNList = new ArrayList<Relation1toN>();

		relations1to1.setRelation1to1List(relation1to1List);
		relations1toN.setRelation1toNList(relation1toNList);

		c.setListOf1to1Relations(listOf1to1Relations);
		c.setListOf1toNRelations(listOf1toNRelations);

		Relation1to1 relation1to1 = new Relation1to1();
		Relation1toN relation1toN = new Relation1toN();

		relation1to1List.add(relation1to1);
		relation1toNList.add(relation1toN);

		try {
			relationContainerValidator.validate(c);
			fail();
		} catch (CotrixException e) {
		}

		relations1to1.setSourceConcept(new Concept("A"));
		relations1toN.setSourceConcept(new Concept("A"));
		relations1to1.setTargetConcept(new Concept("A"));
		relations1toN.setTargetConcept(new Concept("A"));

		try {
			relationContainerValidator.validate(c);
			fail();
		} catch (CotrixException e) {
		}

		relations1to1.setTargetConcept(new Concept("B"));

		try {
			relationContainerValidator.validate(c);
			fail();
		} catch (CotrixException e) {
		}

		relation1to1.setSourceCode(new Code(""));
		relation1to1.setTargetCode(new Code(""));

		relations1to1.setTargetConcept(new Concept("B"));
		relations1toN.setTargetConcept(new Concept("B"));

		try {
			relationContainerValidator.validate(c);
			fail();
		} catch (Exception e) {
		}
		relation1to1.setSourceCode(new Code("A"));
		relation1to1.setTargetCode(new Code("B"));

	}
}
