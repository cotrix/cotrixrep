package org.cotrix.domain.relation;

import org.cotrix.domain.code.Code;
import org.cotrix.domain.coderelation.Relation1toN;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Relation1toNTest {

	@Test
	public void testEquals1() {
		Relation1toN r1 = new Relation1toN();
		Relation1toN r2 = new Relation1toN();
		Code c1 = new Code("A");
		Code c2 = new Code("A");
		r1.setFromCode(c1);
		r2.setFromCode(c2);
		assertTrue(r1.equals(r2));

		Code c3 = new Code("B");
		r2.setFromCode(c3);
		assertFalse(r1.equals(r2));

	}

	@Test
	public void testEquals2() {
		Relation1toN r1 = new Relation1toN();
		Relation1toN r2 = new Relation1toN();
		Code c1 = new Code("1x1");
		Code c2 = new Code("3x1");
		r1.setFromCode(c1);
		r2.setFromCode(c2);
		assertFalse(r1.equals(r2));

	}

}
