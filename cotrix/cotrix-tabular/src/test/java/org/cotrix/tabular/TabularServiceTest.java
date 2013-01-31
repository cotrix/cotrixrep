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
		CodeBag codeBag = p.mine(g.generate(rows, columns));
		assertEquals(columns, codeBag.getCodelistContainer().getList().size());
	}
}
