package org.acme;


import static org.cotrix.action.Action.*;
import static org.cotrix.action.ActionType.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.MainAction.*;
import static org.junit.Assert.*;

import org.cotrix.action.Action;
import org.cotrix.action.MainAction;
import org.junit.Test;

public class ActionTest {

	@Test
	public void isBuiltCorrectly() {
		
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
		
	}
	
	@Test
	public void canBeComparedForEquivalence() {
		
	
		assertEquals(IMPORT,IMPORT);
		assertFalse(IMPORT.equals(MainAction.PUBLISH));
		
		assertEquals(IMPORT.on("1"),IMPORT.on("1"));
		assertFalse(IMPORT.on("1").equals(IMPORT.on("2")));
		
		assertFalse(action(codelist,"a").equals(action(main,"a")));
	}
	
	@Test
	public void inclusionRespectsEnumTypes() {
		
		Action a = action(codelist,"a");
		Action aDifferentA = action(main,"a");
		
		assertFalse(a.included(aDifferentA));
	}
	
	@Test
	public void isInMoreGenericAndNotInLessGeneric() {
		
		Action a = action("a");
		Action ab = action("a","b");
		Action abc = action("a","b","c");
		
		assertTrue(ab.included(a));
		assertTrue(abc.included(a));
		assertFalse(a.included(ab));
		assertFalse(a.included(abc));
	}
	
	
	@Test
	public void isInWildcardExtension() {

		Action anya = action("a",any);
		
		Action a = action("a");
		Action ab = action("a","b");
		Action aanyc = action("a",any,"c");
		
		assertTrue(a.included(anya));
		assertTrue(ab.included(anya));
		assertTrue(aanyc.included(anya));
		
		
	}

	
	@Test
	public void isInOneOfMany() {
		
		Action a = action("a");
		Action ab = action("a","b");
		Action abc = action("a","b","c");
		
		//or semantics
		assertTrue(ab.included(a,abc));
	}
	
	@Test
	public void instanceIsInGenericAndGenericIsNotInstance() {
		
		Action a = action("a");
		Action a1 = action("a").on("1");
		Action ab1 = action("a","b").on("1");
		
		assertTrue(a1.included(a));
		assertTrue(ab1.included(a));
		assertFalse(a1.included(ab1));
	}	
}
