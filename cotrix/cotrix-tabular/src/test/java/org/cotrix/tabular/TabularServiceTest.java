package org.cotrix.tabular;

import static org.junit.Assert.assertEquals;

import org.cotrix.domain.bag.CodeBag;
import org.cotrix.tabular.TabularService;
import org.cotrix.tabular.generator.TabularGenerator;
import org.junit.Test;

public class TabularServiceTest {

	TabularService p = new TabularService();
	TabularGenerator g = new TabularGenerator();

	@Test
	public void testParseTabular() {
		int rows = 10;
		int columns = 5;
		CodeBag codeJar = p.parse(g.generate(rows, columns));
		assertEquals(columns, codeJar.getCodelistContainer().getList().size());
	}
}
