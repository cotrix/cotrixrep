package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.containers.Group;
import org.junit.Test;

public class BagTest {

	@Test
	public void emptyBagsAreCorrectlyConstructed() {
			
		Bag<Attribute> bag = new Bag<Attribute>();
		
		assertFalse(bag.list().iterator().hasNext());
		assertFalse(bag.has(name));
		
		assertTrue(bag.get(name).isEmpty());
		assertTrue(bag.remove(name).isEmpty());
	
	}
	
	@Test
	public void bagsAreCorrectlyUpdated() {
			
		Bag<Attribute> bag = new Bag<Attribute>();
		
		Attribute e = new Attribute(name, value);
	
		assertFalse(bag.has(e.name()));
		assertFalse(bag.has(e));
		assertEquals(0,bag.size());
		
		assertTrue(bag.add(e));
		
		assertTrue(bag.has(e.name()));
		assertTrue(bag.has(e));
		assertEquals(1,bag.size());
		
		//duplicate not permitted
		assertFalse(bag.add(e));
		
		Attribute e2 = new Attribute(name, value2);
		
		assertTrue(bag.add(e2));
		
		assertTrue(bag.has(e2.name()));
		assertTrue(bag.has(e2));
		
		assertEquals(2,bag.size());
		
		List<Attribute> removed = bag.remove(e.name());
		assertTrue(removed.contains(e));
		assertTrue(removed.contains(e2));
		
		assertFalse(bag.has(e.name()));
		assertFalse(bag.has(e));
		assertFalse(bag.has(e2.name()));
		assertFalse(bag.has(e2));
		assertEquals(0,bag.size());
	}
	
	@Test
	public void bagsAreCorrectlyCloned() {
			
		Bag<Attribute> bag = new Bag<Attribute>();
		
		assertTrue(bag.add(new Attribute(name, value)));
		
		Bag<Attribute> clone = bag.copy();
		
		assertEquals(bag,clone);
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
