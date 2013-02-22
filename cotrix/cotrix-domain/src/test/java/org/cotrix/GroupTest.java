package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.BaseGroup;
import org.junit.Test;

public class GroupTest {

	@Test
	public void emptyGroupsAreCorrectlyConstructed() {
			
		BaseGroup<Attribute> attributes = new BaseGroup<Attribute>();
		
		assertEquals(0,attributes.size());
		assertFalse(attributes.contains(name));
		
		try {
			attributes.get(name);
			fail();
		}
		catch(IllegalStateException e) {}
		
		try {
			attributes.remove(name);
			fail();
		}
		catch(IllegalStateException e) {}
	
	}
	
	@Test
	public void elementsAreCorrectlyAdded() {
			
		BaseGroup<Attribute> attributes = new BaseGroup<Attribute>();
		
		Attribute replaced = attributes.add(a);
		
		assertNull(replaced);
		
		assertEquals(a,attributes.iterator().next());
		assertTrue(attributes.contains(name));
		assertEquals(a, attributes.get(name));
		
		replaced = attributes.add(a);
		
		assertEquals(replaced,a);
	}
	
	@Test
	public void elementsAreCorrectlyRemoved() {
			
		BaseGroup<Attribute> attributes = new BaseGroup<Attribute>();
		
		attributes.add(a);
		Attribute removed = attributes.remove(name);
		
		assertEquals(a,removed);
		
		assertEquals(0,attributes.size());
		assertFalse(attributes.contains(name));
		
	}
}
