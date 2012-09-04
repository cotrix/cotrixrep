package org.cotrix.tabular.mining;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.domain.code.Code;
import org.cotrix.domain.code.Codelist;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.tabular.generator.TabularGenerator;
import org.cotrix.tabular.generator.UniquenessRule;
import org.cotrix.tabular.mining.CodeMiner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodeMinerTest {

	CodeMiner codeMiner = new CodeMiner();
	TabularGenerator g = new TabularGenerator();

	@Test
	public void testParseCodes() {
		int rows = 10;
		int columns = 5;
		CodelistContainer c = codeMiner.parseCodes(g.generate(rows, columns));
		List<Codelist> l = c.getList();
		assertEquals(columns, l.size());

		// UniquenessRule.DUPLICATES;
		// there need to be at least 2 lists with only unique elements
		int count = 0;
		for (Codelist codelist : l) {
			List<Code> list = codelist.getCodelist();
			Set<Code> set = new HashSet<Code>();
			for (Code code : list) {
				set.add(code);
			}
			System.out.println(set.size());
			if (set.size() == rows) {
				count++;
			}
		}
		System.out.println(count + " ");

		assertTrue(UniquenessRule.DUPLICATES <= count);

	}
}
