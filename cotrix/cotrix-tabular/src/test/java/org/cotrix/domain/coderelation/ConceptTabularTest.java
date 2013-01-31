package org.cotrix.domain.coderelation;

import org.cotrix.domain.coderelation.ConceptTabular;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ConceptTabularTest {

	@Test
	public void testEquals() {
		ConceptTabular c1 = new ConceptTabular(1);
		ConceptTabular c2 = new ConceptTabular(2);
		assertEquals(c1, c1);
		assertNotSame(c1, c2);
	}

}
