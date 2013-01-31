package org.cotrix.tabular.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.domain.tabular.Tabular;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TabularGeneratorTest extends TabularGenerator {

	TabularGenerator g = new TabularGenerator();

	@Test
	public void testGenerate1() {
		int nr = 2;
		int nc = 4;
		delegate(nr, nc);
	}

	@Test
	public void testGenerate23() {
		int nr = 25;
		int nc = 10;
		delegate(nr, nc);
	}

	@Test
	public void testGenerate3() {
		for (int nr = 1; nr < 13; nr++) {
			for (int nc = 1; nc < 13; nc++) {
				delegate(nr, nc);
			}
		}
	}

	private void delegate(int nr, int nc) {
		Tabular t = g.generate(nr, nc);

		List<String> headers = t.getHeader();
		for (String header : headers) {
			assertNotNull(header);
		}

		List<List<String>> rows = t.getRows();
		assertEquals(nr, rows.size());
		for (List<String> row : rows) {
			assertEquals(nc, row.size());
		}
		// UniquenessRule.DUPLICATES;
		// there need to be at least 2 lists with only unique elements
		if (nc > UniquenessRule.DUPLICATES) {
			int count = 0;
			for (int c = 0; c < nc; c++) {
				Set<String> set = new HashSet<String>();
				for (int r = 0; r < nr; r++) {
					String code = t.getRows().get(r).get(c);
					set.add(code);
				}
				if (set.size() == nr) {
					count++;
				}
			}
			assertTrue(UniquenessRule.DUPLICATES <= count);
		}

	}
}
