package org.cotrix.tabular.generator;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UniquenessRuleTest extends UniquenessRule {

	UniquenessRule r = new UniquenessRule();

	@Test
	public void testCalculateUniqueness1() {
		try {
			r.calculateUniqueness(0);
			fail();
		} catch (Exception e) {
		}

		for (int i = 1; i < 100; i++) {
			Double[] u = r.calculateUniqueness(i);
			assertEquals(i, u.length);
		}

		int n = 1;
		Double[] e1 = { 1.0 };
		assertArrayEquals(e1, r.calculateUniqueness(n));

	}

	@Test
	public void testCalculateUniqueness2() {
		int n = 2;
		Double[] e1 = { 1.0, 1.0 };
		assertArrayEquals(e1, r.calculateUniqueness(n));
	}

	@Test
	public void testCalculateUniqueness3() {
		int n = 3;
		Double[] e1 = { 1.0, 1.0, 0.0 };
		assertArrayEquals(e1, r.calculateUniqueness(n));
	}

	@Test
	public void testCalculateUniqueness4() {
		int n = 6;
		Double[] e1 = { 1.0, 1.0, 0.5, 0.5, 0.0, 0.0 };
		assertArrayEquals(e1, r.calculateUniqueness(n));
	}

	@Test
	public void testCalculateUniquenessRandom() {
		int n = 100;
		Double[] o = r.calculateUniqueness(n);
		Double[] ra = r.calculateUniquenessRandom(n);
		int equals = 0;
		for (int i = 0; i < o.length; i++) {
			if (o[i].equals(ra[i])) {
				equals++;
			}
		}
		// System.out.println(equals);
		assertTrue(equals < 10);
	}

	@Test
	public void testCalculateNrOf1to1() {
		assertEquals(2, r.calculateNrOf1to1(3));
		assertEquals(6, r.calculateNrOf1to1(6));
		assertEquals(6, r.calculateNrOf1to1(7));
		assertEquals(8, r.calculateNrOf1to1(8));

	}

	@Test
	public void testCalculateNrOf1toN() {
		assertEquals(0, r.calculateNrOf1toN(1));
		assertEquals(0, r.calculateNrOf1toN(2));
		assertEquals(2, r.calculateNrOf1toN(3));
		assertEquals(4, r.calculateNrOf1toN(4));
	}

}
