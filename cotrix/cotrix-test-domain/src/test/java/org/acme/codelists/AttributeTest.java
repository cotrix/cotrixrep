package org.acme.codelists;

import static org.junit.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;

import org.acme.DomainTest;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.AttributeMS;
import org.junit.Test;

public class AttributeTest extends DomainTest {
	
	@Test
	public void attributesCanBeFluentlyConstructed() {
		
		Attribute a = attribute().name(name).value(value).build();
		
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(DEFAULT_TYPE,a.type());
		
		a = attribute().name(name).value(value).ofType(type).build();
		
		assertEquals(type,a.type());
		
		a = attribute().name(name).value(value).ofType(name).in(language).build();
		
		assertEquals(language,a.language());

		//other sentences
		attribute().name(name).value(value).in(language).build();
	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		Attribute a;
		
		//new attributes
		 a =  attribute().name(name).build();
		 a =  attribute().name(name).value(value).build();
		 a =  attribute().name(name).value(value).ofType("type").build();
		 a =  attribute().name(name).value(value).ofType("type").in("en").build();
		 a =  attribute().name(name).value(value).in("en").build();
		 
		 
		assertFalse(reveal(a).isChangeset());
		assertNull(reveal(a).status());
			
		//modified attributes
		 a = modifyAttribute("1").name(name).build();
		 a = modifyAttribute("1").name(name).value(value).build();
		 a = modifyAttribute("1").ofType("type").build();
		 a = modifyAttribute("1").ofType("type").in("en").build();
		 a = modifyAttribute("1").in("en").build();

		assertTrue(reveal(a).isChangeset());
		assertEquals(MODIFIED,reveal(a).status());		
		
		//removed attributes
		a = deleteAttribute("1");

		assertTrue(reveal(a).isChangeset());
		assertEquals(DELETED,reveal(a).status());		
	}


	@Test
	public void cloned() {
		
		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		
		Attribute.State state = reveal(a).state();
		AttributeMS clone = new AttributeMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void emptyChangeset() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		
		Attribute changeset = modifyAttribute(a.id()).build();
		
		reveal(a).update(reveal(changeset)); 

		assertEquals(name, a.name());
		assertEquals(value, a.value());
		assertEquals(type, a.type());
		assertEquals(language, a.language());
	}
	
	@Test
	public void changesAttributes() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		
		Attribute changeset = modifyAttribute(a.id()).name(name2).value(value2).ofType(type2).in(language2).build();
		
		reveal(a).update(reveal(changeset));

		assertEquals(name2, a.name());
		assertEquals(value2, a.value());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
	}
	
	

	@Test
	public void cannotErasetNameOfAttributes() {

		
		Attribute a = like(attribute().name(name).build());
		Attribute changeset = modifyAttribute(a.id()).name(NULL_QNAME).build();
		
		try {
			reveal(a).update(reveal(changeset));
			fail();
		}
		catch(IllegalArgumentException e) {}

	}

	@Test
	public void erasesValue() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		Attribute changeset = modifyAttribute(a.id()).value(NULL_STRING).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.value());
		assertEquals(type,a.type());
		assertEquals(language,a.language());
	}
	
	@Test
	public void erasesType() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
	
		Attribute changeset = modifyAttribute(a.id()).ofType(NULL_QNAME).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.type());
		assertEquals(name,a.name());
		assertEquals(language,a.language());
	}
	
	@Test
	public void erasesLanguage() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
	
		Attribute changeset = modifyAttribute(a.id()).in(NULL_STRING).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.language());
		assertEquals(name,a.name());
		assertEquals(type,a.type());
	}
}
