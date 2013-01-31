package org.cotrix.tabular;

import static org.junit.Assert.assertEquals;

import org.cotrix.domain.bag.CodeBag;
import org.cotrix.domain.tabularmeta.TabularMeta;
import org.cotrix.tabular.generator.TabularGenerator;
import org.junit.Test;

public class TabularServiceTest {

	TabularService p = new TabularService();
	TabularGenerator g = new TabularGenerator();

	/**
	 * @TODO this test is by far not completed.
	 * 
	 * @TODO Besides testing with the TabularGenerator, also a test needs to be written using the real ASFIS species
	 *       list: /cotrix-tabular/src/test/resources/ASFIS/2012/original/ASFIS_sp_Feb_2012.txt
	 */
	@Test
	public void testParse() {
		int rows = 10;
		int columns = 5;
		TabularMeta tabularMeta = new TabularMeta();

		CodeBag codeBag = p.parse(g.generate(rows, columns), tabularMeta);
		assertEquals(columns, codeBag.getCodelistContainer().getList().size());
	}

	@Test
	public void testMine() {
		int rows = 10;
		int columns = 5;
		CodeBag codeBag = p.mine(g.generate(rows, columns));
		assertEquals(columns, codeBag.getCodelistContainer().getList().size());
	}
}
