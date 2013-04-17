package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Container;
import org.cotrix.domain.LanguageAttribute;
import org.junit.Test;

public class UpdateTest {

	//we answer the question: can DOs be correctly updated?

	
	//update validation and change of name is performed by base class. if it works for attributes it will work for all DOs
	//we should still be testing invocation of super(), but what the heck... 
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireIdentifiers() {
		
		Attribute.Private a = (Attribute.Private)attr().name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private) attr("1").name(name2).value(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireDeltas() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name2).value(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireMatchingIds() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("different").name(name2).value(value).build();
		
		a.update(delta);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updatesRequireModifiedDeltas() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name2).value(value).as(NEW).build();
		
		a.update(delta);
		
	}
	
	
	
	@Test
	public void DOsCanChangeName() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name2).value(value).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(name2,a.name());
	}
	
	@Test
	public void attributesCanChangeValue() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name).value(value2).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(value2,a.value());
	}
	
	@Test
	public void attributesCanChangeType() {
		
		Attribute.Private a = (Attribute.Private)attr("1").name(name).value(value).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name).value(value).ofType(type).as(MODIFIED).build();
		
		a.update(delta);
		
		assertEquals(type,a.type());
	}
	
	@Test
	public void attributesCanChangeLanguage() {
		
		LanguageAttribute.Private a = (LanguageAttribute.Private) attr("1").name(name).value(value).in(language).build();
		
		Attribute.Private delta = (Attribute.Private)attr("1").name(name).value(value).in("another").as(MODIFIED).build();
		
		assertFalse(a.language().equals("another"));
		
		a.update(delta);
		
		assertEquals("another",a.language());
	}
	
	///     bags
	
	@Test
	public void bagsCanBeUpdated() {

		Attribute.Private a1 = (Attribute.Private)attr("1").name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private)attr("2").name(name2).value(value2).build();
		
		Container.Private<Attribute.Private> bag = bag(a1,a2);
		
		//a change
		Attribute.Private deltaA1 = (Attribute.Private)attr("1").name(name).value(value+"updated").as(MODIFIED).build();
		
		// a deletion
		Attribute.Private deltaA2 = (Attribute.Private)attr("2").name(name2).value(value2).as(DELETED).build();
		
		// a removal
		Attribute.Private deltaA3 = (Attribute.Private)attr().name(name3).value(value3).as(NEW).build();
		
		Container.Private<Attribute.Private> delta = bag(deltaA1,deltaA2,deltaA3);
		
		bag.update(delta);
		
		//changed stuff is updated
		assertEquals(value+"updated",asMap(bag).get(name).get(0).value());
		//removed stuff is ... removed
		assertFalse(asMap(bag).containsKey(name2));
		//new stuff is added
		assertTrue(asMap(bag).containsKey(name3));
		
		//new stuff no longer a delta object
		assertNull(asMap(bag).get(name3).get(0).change());
		
	}
	
}
