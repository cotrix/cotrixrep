package org.cotrix.tabular.mining;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.domain.code.Code;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.coderelation.Relation1to1;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;
import org.cotrix.tabular.generator.TabularGenerator;
import org.cotrix.tabular.generator.UniquenessRule;
import org.cotrix.tabular.model.Tabular;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RelationMinerTest {
	RelationMiner m = new RelationMiner();
	UniquenessRule u = new UniquenessRule();
	CodeMiner codeMiner = new CodeMiner();
	TabularGenerator g = new TabularGenerator();

	CodelistContainer codelistContainer;
	Tabular tabular;

	@Test
	public void testCalculateRelations1toNList() {
		int rows = 10;
		int columns = 2;
		tabular = g.generate(rows, columns);
		codelistContainer = codeMiner.parseCodes(tabular);
		RelationContainer r = new RelationContainer();
		m.calculateRelations1toNList(tabular, r);
		int expand = u.calculateNrOf1to1(columns) + u.calculateNrOf1toN(columns);
		assertEquals(expand, r.getListOf1toNRelations().size());
	}

	/**
	 * TODO activate the loop and find the errors
	 */
	@Test
	public void testMine() {
		// for (int c = 2; c < 5; c++) {
		// for (int r = 2; r < 5; r++) {
		// System.out.println(r + " " + c);
		// testDeletate(r, c);
		// }
		// }

		testDeletate(2, 2);

	}

	public void testDeletate(int testRows, int testColumns) {
		tabular = g.generate(testRows, testColumns);
		codelistContainer = codeMiner.parseCodes(tabular);

		RelationContainer c = m.mine(tabular, codelistContainer);
		List<Relations1toN> relations1toNList = c.getListOf1toNRelations();
		List<Relations1to1> relations1to1List = c.getListOf1to1Relations();

		assertEquals(u.calculateNrOf1to1(testColumns), relations1to1List.size());
		assertEquals(u.calculateNrOf1toN(testColumns), relations1toNList.size());
		for (Relations1toN relations1toN : relations1toNList) {
			List<Relation1toN> list = relations1toN.getRelation1toNList();
			// System.out.println("Source concept = "
			// + ((ConceptTabular)
			// relations1toN.getSourceConcept()).getColumnNumber());
			for (Relation1toN relation1toN : list) {
				// System.out.println("  getFromCode = " +
				// relation1toN.getFromCode().getValue());
				assertNotNull(relation1toN.getFromCode());
				List<Code> to = relation1toN.getToCollection();
				Set<String> set = new HashSet<String>();
				for (Code code : to) {
					assertNotNull(code);
					// System.out.println("    to code " + code.getValue());
					set.add(code.getValue());
				}
				assertEquals(to.size(), set.size());
				assertNotNull(relation1toN.getFromCode());
				assertTrue(list.size() > 0);
			}
		}

		int countUnique = 0;
		for (Relations1to1 relations1to1 : relations1to1List) {
			if (relations1to1.getRelation1to1List().size() == testRows) {
				countUnique++;
			}
			List<Relation1to1> list = relations1to1.getRelation1to1List();
			for (Relation1to1 relation1to1 : list) {
				assertNotNull(relation1to1.getSourceCode());
				assertNotNull(relation1to1.getTargetCode());

			}
		}
		assertEquals(u.calculateNrOfUniqueColumns(testColumns), countUnique);

	}
}
