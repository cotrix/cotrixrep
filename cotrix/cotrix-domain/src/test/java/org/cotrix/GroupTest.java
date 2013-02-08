package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.containers.Group;
import org.junit.Test;

public class GroupTest {

	@Test
	public void emptyGroupsAreCorrectlyConstructed() {
			
		Group<Attribute> attributes = new Group<Attribute>();
		
		assertFalse(attributes.list().iterator().hasNext());
		assertFalse(attributes.has(name));
		
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
	public void groupsAreCorrectlyCloned() {
			
		Group<Attribute> attributes = new Group<Attribute>();
		
		attributes.add(new Attribute(name, value));
		
		Group<Attribute> clone = attributes.copy();
		
		assertEquals(attributes,clone);
	}
	
	@Test
	public void elementsAreCorrectlyAdded() {
			
		Group<Attribute> attributes = new Group<Attribute>();
		
		Attribute attribute = new Attribute(name, value);
		
		Attribute replaced = attributes.add(attribute);
		
		assertNull(replaced);
		
		assertEquals(attribute,attributes.list().iterator().next());
		assertTrue(attributes.has(name));
		assertEquals(attribute, attributes.get(name));
		
		replaced = attributes.add(attribute);
		
		assertEquals(replaced,attribute);
	}
	
	@Test
	public void elementsAreCorrectlyRemoved() {
			
		Group<Attribute> attributes = new Group<Attribute>();
		
		Attribute attribute = new Attribute(name, value);
		
		attributes.add(attribute);
		Attribute removed = attributes.remove(name);
		
		assertEquals(attribute,removed);
		
		assertFalse(attributes.list().iterator().hasNext());
		assertFalse(attributes.has(name));
		
	}
}
