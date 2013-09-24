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
		
		Action a = type(any).end();
		
		assertEquals(asList(any),a.parts());
		assertNull(a.instance());
		
		a = type("a").end();
		assertEquals(asList("a"),a.parts());
		assertEquals(null,a.instance());
		
		a = type("a").op("b").end();
		assertEquals(asList("a","b"),a.parts());
		assertEquals(null,a.instance());
		
		a = type("a").op("b").op("c").on("d");
		
		assertEquals(asList("a","b","c"),a.parts());
		assertEquals("d",a.instance());
		
		a = type("a").on("c");
		
		assertEquals(asList("a"),a.parts());
		assertEquals("c",a.instance());
	}
	
	@Test
	public void cannotBeBuiltIncorrectly() {
		
		try {
			action(null);
			fail();
		}
		catch(IllegalArgumentException e){}
		
		try {
			action("");
			fail();
		}
		catch(IllegalArgumentException e){}
		
		
		try {
			action("a","");
			fail();
		}
		catch(IllegalArgumentException e){}
		
		try {
			actionOn(null,"a");
			fail();
		}
		catch(IllegalArgumentException e){}
		
		try {
			actionOn("","a");
			fail();
		}
		catch(IllegalArgumentException e){}
		
		
	}
	
	
	@Test
	public void matchSameOrEquals() {
		
		Action a = type("a").end();
		Action sameAsA = type("a").end();

		assertTrue(a.isIn(a));
		
		assertTrue(a.isIn(sameAsA));
	}
	
	@Test
	public void matchGeneralisations() {
		
		Action a = type("a").end();
		Action a1 = type("a").op("1").end();
		Action a12 = type("a").op("1").op("2").end();
		
		assertTrue(a1.isIn(a));
		
		assertTrue(a12.isIn(a));
	}
	
	@Test
	public void doNotMatchSpecialisations() {
		
		Action a = type("a").end();
		Action a1 = type("a").op("1").end();
		
		assertFalse(a.isIn(a1));
	}
	
	@Test
	public void matchOneOfMany() {
		
		Action a = type("a").end();
		Action a1 = type("a").op("1").end();
		Action a2 = type("a").op("2").end();
		
		//or semantics
		assertTrue(a1.isIn(a2,a));
	}
	
	@Test
	public void matchWildCardGeneralisations() {

		
		Action top = action(any);
		Action a = action("a");
	
		//top-level wildcard
		assertTrue(a.isIn(top));
	}
	
	@Test
	public void matchWildCardExtensions() {

		Action anya = action("a",any);
		
		Action a = action("a");
		Action a1 = action("a","1");
		
		//inner-level wildcard vs. empty
		assertTrue(a.isIn(anya));
		
		//inner-level wildcard vs. any
		assertTrue(a1.isIn(anya));
		
	}
}
