package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.traits.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.BaseBag;
import org.junit.Test;

public class UpdateTest {


	
	@Test
	public void bagsCanBeUpdated() {

		Attribute a1 = a("1").with(name).and(value).build();
		Attribute a2 = a("2").with(name2).and(value2).build();
		
		BaseBag<Attribute> bag = attributes(a1,a2);
		
		//a change
		Attribute deltaA1 = a("1").with(name).and(value+"updated").as(MODIFIED).build();
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
		assertNull(bag.get(name3).get(0).change());
		
	}
}
