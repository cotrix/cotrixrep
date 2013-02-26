package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.traits.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.LanguageAttribute;
import org.cotrix.domain.primitives.BaseBag;
import org.cotrix.domain.primitives.BaseGroup;
import org.junit.Test;

public class UpdateTest {

	//we answer the question: can DOs be correctly updated?

	
	//update validation and change of name is performed by base class. if it works for attributes it will work for all DOs
	//we should still be testing invocation of super(), but what the heck... 
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireIdentifiers() {
		
		Attribute a = a().with(name).and(value).build();
		
		Attribute delta = a("1").with(name2).and(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireDeltas() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("1").with(name2).and(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireMatchingIds() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("different").with(name2).and(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireModifiedDeltas() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("1").with(name2).and(value).as(NEW).build();
		
		a.update(delta);
		
	}
	
	
	
	@Test
	public void DOsCanChangeName() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("1").with(name2).and(value).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(name2,a.name());
	}
	
	@Test
	public void attributesCanChangeValue() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("1").with(name).and(value2).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(value2,a.value());
	}
	
	@Test
	public void attributesCanChangeType() {
		
		Attribute a = a("1").with(name).and(value).build();
		
		Attribute delta = a("1").with(name).and(value).ofType(type).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(type,a.type());
	}
	
	@Test
	public void attributesCanChangeLanguage() {
		
		LanguageAttribute a = (LanguageAttribute) a("1").with(name).and(value).in(language).build();
		
		Attribute delta = a("1").with(name).and(value).in("another").as(MODIFIED).build();
		
		assertFalse(a.language().equals("another"));
		
		a.update(delta);
		
		assertEquals("another",a.language());
	}
	
	///     bags
	
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
	
	///     groups
	
	@Test
	public void groupsCanBeUpdated() {

		Attribute a1 = a("1").with(name).and(value).build();
		Attribute a2 = a("2").with(name2).and(value2).build();
		
		BaseGroup<Attribute> group = group(a1,a2);
		
		//a change
		Attribute deltaA1 = a("1").with(name).and(value+"updated").as(MODIFIED).build();
		
		// a deletion
		Attribute deltaA2 = a("2").with(name2).and(value2).as(DELETED).build();
		
		// a removal
		Attribute deltaA3 = a().with(name3).and(value3).as(NEW).build();
		
		BaseGroup<Attribute> delta = group(deltaA1,deltaA2,deltaA3);
		
		group.update(delta);
		
		//changed stuff is updated
		assertEquals(value+"updated",group.get(name).value());
		//removed stuff is ... removed
		assertFalse(group.contains(name2));
		//new stuff is added
		assertTrue(group.contains(name3));
		
		//new stuff no longer a delta object
		assertNull(group.get(name3).change());
		
	}
	
}
