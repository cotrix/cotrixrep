package org.acme;


import static org.cotrix.action.Actions.*;
import static org.junit.Assert.*;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.GenericAction;
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
	public void isAnAny() {
		
		Action ab = action("a","b");
		Action top = action(any);

		assertTrue(ab.isIn(top));
		
	}
	
	@Test
	public void typedIsAnAny() {
		
		Action top = allActions;

		assertTrue(GenericAction.IMPORT.isIn(top));
		
	}
	
	@Test
	public void inclusionRespectsEnumTypes() {
		
		Action a = action(CodelistAction.class,"a");
		Action aDifferentA = action(GenericAction.class,"a");
		
		assertFalse(a.isIn(aDifferentA));
	}
	
	@Test
	public void isInMoreGenericAndNotInLessGeneric() {
		
		Action a = action("a");
		Action ab = action("a","b");
		Action abc = action("a","b","c");
		
		assertTrue(ab.isIn(a));
		assertTrue(abc.isIn(a));
		assertFalse(a.isIn(ab));
		assertFalse(a.isIn(abc));
	}
	

	
	
	@Test
	public void isInWildcard() {

		
		Action top = action(any);
		Action a = action("a");
	
		assertTrue(a.isIn(top));
		assertFalse(top.isIn(a));
	}
	
	@Test
	public void isInWildcardExtension() {

		Action anya = action("a",any);
		
		Action a = action("a");
		Action ab = action("a","b");
		Action aanyc = action("a",any,"c");
		
		assertTrue(a.isIn(anya));
		assertTrue(ab.isIn(anya));
		assertTrue(aanyc.isIn(anya));
		
		
	}

	
	@Test
	public void isInOneOfMany() {
		
		Action a = action("a");
		Action ab = action("a","b");
		Action abc = action("a","b","c");
		
		//or semantics
		assertTrue(ab.isIn(a,abc));
	}
	
	@Test
	public void instanceIsInGenericAndGenericIsNotInstance() {
		
		Action a = action("a");
		Action a1 = action("a").on("1");
		Action ab1 = action("a","b").on("1");
		
		assertTrue(a1.isIn(a));
		assertTrue(ab1.isIn(a));
		assertFalse(a.isIn(a1));
	}	
}
