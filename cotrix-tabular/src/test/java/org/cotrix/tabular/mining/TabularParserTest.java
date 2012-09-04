package org.cotrix.tabular.mining;

import static org.junit.Assert.assertEquals;

import org.cotrix.domain.bag.CodeBag;
import org.cotrix.tabular.generator.TabularGenerator;
import org.cotrix.tabular.mining.TabularParser;
import org.junit.Test;

public class TabularParserTest {

	TabularParser p = new TabularParser();
	TabularGenerator g = new TabularGenerator();

	@Test
	public void testParseTabular() {
		int rows = 10;
		int columns = 5;
		CodeBag codeJar = p.parse(g.generate(rows, columns));
		assertEquals(columns, codeJar.getCodelistContainer().getList().size());
	}
}
