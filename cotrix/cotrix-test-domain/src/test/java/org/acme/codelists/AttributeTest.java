package org.acme.codelists;

import static org.junit.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
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
	public void typedAttributesCanBeFluentlyConstructed() {
		
		Definition def = definition().name(name).build();
	
		Attribute a = attribute().with(def).value(value).build();
		
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(def,a.definition());
		assertEquals(DEFAULT_TYPE,a.type());
		
		Attribute a2 = attribute().with(def).build();
		
		assertEquals(a.definition(),a2.definition());
		
	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		Attribute a;
		
		 a = modifyAttribute("1").name(name).build();
		 a = modifyAttribute("1").name(name).value(value).build();
		 a = modifyAttribute("1").ofType(type).build();
		 a = modifyAttribute("1").ofType(type).in("en").build();
		 a = modifyAttribute("1").in("en").build();

		assertTrue(reveal(a).isChangeset());
		assertEquals(MODIFIED,reveal(a).status());		
	
		//removed attributes
		a = deleteAttribute("1");

		assertTrue(reveal(a).isChangeset());
		assertEquals(DELETED,reveal(a).status());		
	}
	
	@Test
	public void typedChangesetsCanBeFluentlyConstructed() {

		Definition def = definition().name(name).build();
		
		modifyAttribute("1").value(value).build();
		modifyAttribute("1").with(def).build();
		modifyAttribute("1").with(def).value(value).build();

	}


	@Test
	public void cloned() {
		
		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		
		Attribute.State state = reveal(a).state();
		AttributeMS clone = new AttributeMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void clonedTyped() {
		
		Definition type = like(definition().name(name).in(language).build());
		
		Attribute a = like(attribute().with(type).value(value).build());
		
		Attribute.State state = reveal(a).state();
		AttributeMS clone = new AttributeMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void emptyTypedChangeset() {

		Definition type = like(definition().name(name).in(language).build());
		
		Attribute a = like(attribute().with(type).value(value).build());
		
		Attribute changeset = modifyAttribute(a.id()).build();
		
		reveal(a).update(reveal(changeset)); 

		assertEquals(name, a.name());
		assertEquals(value, a.value());
		assertEquals(type, a.definition());
		assertEquals(language, a.language());
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
	public void changeAll() {

		Attribute a = like(attribute().name(name).value(value).ofType(type).in(language).build());
		
		Attribute changeset = modifyAttribute(a.id()).name(name2).value(value2).ofType(type2).in(language2).build();
		
		reveal(a).update(reveal(changeset));

		assertEquals(name2, a.name());
		assertEquals(value2, a.value());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
	}
	
	@Test
	public void changeType() {
		
		Definition type = like(definition().name(name).build());

		Definition typeChangeset = modifyDefinition(type.id()).name(name2).build();
		
		Attribute a = like(attribute().with(type).build());
		
		Attribute changeset = modifyAttribute(a.id()).with(typeChangeset).build();
		
		reveal(a).update(reveal(changeset));

		assertEquals(name2, a.name());
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
