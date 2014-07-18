package org.acme.utils;

import static org.junit.Assert.*;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.io.utils.SharedDefinitionPool;
import org.junit.Test;

public class DefinitionPoolTest {

	
	@Test
	public void pools() {
		
		SharedDefinitionPool pool = new SharedDefinitionPool();
		
		AttributeDefinition def = pool.get("n","t","l");
		
		assertSame(def,pool.get("n","t","l"));
		assertNotEquals(def, pool.get("n","t","l2"));
	}
}
