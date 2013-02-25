package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import java.util.UUID;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.utils.IdGenerator;
import org.junit.Test;

public class VersioningTest {

	static IdGenerator generator = new IdGenerator() {
		
		@Override
		public String generateId() {
			return UUID.randomUUID().toString();
		}
	};
	
	@Test
	public void bagsCanBeCopied() {
		
		BaseBag<Attribute> bag = attributes(a,a2);
		
		BaseBag<Attribute> clone = bag.copy(generator);
		
		//ids should have changed hence cannot use equals
		
		assertEquals(bag.change(),clone.change());
		assertEquals(2,clone.size());
		assertTrue(clone.contains(a.name()));
		assertTrue(clone.contains(a2.name()));
		
		//object ids have changed?
		Attribute attr = clone.get(a.name()).get(0);
		assertFalse(attr.id().equals(a.id()));
		
		
	}
	
}
