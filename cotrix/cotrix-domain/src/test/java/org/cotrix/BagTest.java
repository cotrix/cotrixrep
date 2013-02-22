package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.common.Delta.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.List;
import java.util.UUID;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.Delta;
import org.cotrix.domain.utils.IdGenerator;
import org.junit.Test;

public class BagTest {

	//NOTE: we test DOs independently from DSL and use utility methods to construct POs and make our tests less verbose
	//NOTE: we assume tested dependencies, hence we use pre-prepared ones in fixture
	
	static IdGenerator generator = new IdGenerator() {
		
		@Override
		public String generateId() {
			return UUID.randomUUID().toString();
		}
	};
	
	@Test
	public void emptyBagsAreCorrectlyConstructed() {
			
		BaseBag<Attribute> bag = attributes();
		
		assertNull(bag.delta());
		
		assertEquals(0,bag.size());
		assertFalse(bag.contains(name));
		assertTrue(bag.get(name).isEmpty());
		assertTrue(bag.remove(name).isEmpty());
	
	}
	
	@Test
	public void baseBagsAreCorrectlyUpdated() {
			
		BaseBag<Attribute> bag = attributes();
		
		assertFalse(bag.contains(a.name()));
		assertFalse(bag.contains(a));
		assertEquals(0,bag.size());
		
		assertTrue(bag.add(a));
		
		assertTrue(bag.contains(a.name()));
		assertTrue(bag.contains(a));
		assertEquals(1,bag.size());
		
		//duplicate not permitted
		assertFalse(bag.add(a));
		
		assertTrue(bag.add(a2));
		
		assertTrue(bag.contains(a2.name()));
		assertTrue(bag.contains(a2));
		
		assertEquals(2,bag.size());
		
		List<Attribute> removed = bag.remove(a.name());
		assertTrue(removed.contains(a));
		assertFalse(removed.contains(a2));
		
		assertFalse(bag.contains(a.name()));
		assertFalse(bag.contains(a));
		assertTrue(bag.contains(a2.name()));
		assertTrue(bag.contains(a2));
		assertEquals(1,bag.size());
	}
	
	@Test
	public void bagsCanBeCopied() {
		
		BaseBag<Attribute> bag = attributes(a,a2);
		bag.setDelta(Delta.NEW);
		
		BaseBag<Attribute> clone = bag.copy(generator);
		
		//ids should have changed hence cannot use equals
		
		assertEquals(bag.delta(),clone.delta());
		assertEquals(2,clone.size());
		assertTrue(clone.contains(a.name()));
		assertTrue(clone.contains(a2.name()));
		
		//object ids have changed?
		Attribute attr = clone.get(a.name()).get(0);
		assertFalse(attr.id().equals(a.id()));
		
		
	}
	
	@Test
	public void deltaBagsInheritDeltaStatus() {
		
		Attribute attr1 = a("1").with(name).and(value).as(CHANGED).build();
		BaseBag<Attribute> bag = attributes();
		
		assertTrue(bag.add(attr1));
		
		assertEquals(Delta.CHANGED,bag.delta());
		
		////
		
		attr1.setDelta(Delta.NEW);
		bag = attributes();
		
		assertTrue(bag.add(attr1));
		
		assertEquals(Delta.CHANGED,bag.delta());
		
		////
		
		attr1.setDelta(Delta.DELETED);
		
		bag = attributes();
		
		assertTrue(bag.add(attr1));
		
		assertNull(bag.delta());
	}
	
	@Test
	public void bagsCanBeUpdated() {

		Attribute a1 = a("1").with(name).and(value).build();
		Attribute a2 = a("2").with(name2).and(value2).build();
		
		BaseBag<Attribute> bag = attributes(a1,a2);
		
		//a change
		Attribute deltaA1 = a("1").with(name).and(value+"updated").as(CHANGED).build();
		// a deletion
		Attribute deltaA2 = a("2").with(name2).and(value2).as(DELETED).build();
		// a removal
		Attribute deltaA3 = a().with(name3).and(value3).as(NEW).build();
		
		BaseBag<Attribute> delta = attributes(deltaA1,deltaA2,deltaA3);
		
		bag.update(delta);
		
		//changed stuff is updated
		assertEquals(value+"updated",bag.get(name).get(0).value());
		//removed stuff is ... removed
		assertFalse(bag.contains(name2));
		//new stuff is added
		assertTrue(bag.contains(name3));
		
		//new stuff no longer a delta object
		assertNull(bag.get(name3).get(0).delta());
		
	}
	
	@Test
	public void bagsCanBeDeleted() {

		Attribute a1 = a("1").with(name).and(value).build();
		Attribute a2 = a("2").with(name2).and(value2).build();
		
		BaseBag<Attribute> bag = attributes(a1,a2);
		
		BaseBag<Attribute> delta = attributes();
		delta.setDelta(DELETED);
		
		bag.update(delta);
		
		//bag is cleared
		assertEquals(0,bag.size());
		
	}
}
