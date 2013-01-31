package org.cotrix.tabular.mining;

import org.cotrix.domain.coderelation.Relations1toN;
import org.cotrix.tabular.mining.ConceptTabular;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Relations1toNTest {

	@Test
	public void testEquals1() {
		ConceptTabular s1 = new ConceptTabular(1);
		ConceptTabular s2 = new ConceptTabular(2);
		ConceptTabular s3 = new ConceptTabular(3);

		Relations1toN r1 = new Relations1toN();
		r1.setSourceConcept(s1);
		r1.setTargetConcept(s2);
		Relations1toN r2 = new Relations1toN();
		r2.setSourceConcept(s1);
		r2.setTargetConcept(s2);

		assertTrue(r1.equals(r2));

		r2.setTargetConcept(s3);
		assertFalse(r1.equals(r2));

	}

}
