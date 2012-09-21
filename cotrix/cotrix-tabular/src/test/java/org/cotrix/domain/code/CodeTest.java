package org.cotrix.domain.code;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeTest {

	@Test
	public void testEqualsObject() {
		Code code1 = new Code("1");
		code1.setValue("1");
		Code code2 = new Code();
		code2.setValue("1");
		assertTrue(code1.equals(code2));

		code2.setValue("C");
		assertFalse(code1.equals(code2));
	}

}
