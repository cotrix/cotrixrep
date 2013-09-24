package org.acme;


import static java.util.Arrays.*;
import static org.cotrix.engine.Action.*;
import static org.cotrix.engine.ActionBuilder.*;
import static org.junit.Assert.*;

import org.cotrix.engine.Action;
import org.junit.Test;

public class ActionTest {

	@Test
	public void canBeBuiltIncrementally() {
		
		Action a = any();
		
		assertEquals(asList(any),a.parts());
		assertNull(a.instance());
		
		a = type("a").end();
		assertEquals(asList("a"),a.parts());
		assertEquals(null,a.instance());
		
		a = type("a").op("b").end();
		assertEquals(asList("a","b"),a.parts());
		assertEquals(null,a.instance());
		
		a = type("a").op("b").on("c");
		
		assertEquals(asList("a","b"),a.parts());
		assertEquals("c",a.instance());
	}
}
