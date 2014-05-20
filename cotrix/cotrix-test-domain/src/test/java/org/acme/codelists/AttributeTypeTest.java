package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.common.OccurrenceRanges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.memory.AttributeTypeMS;
import org.junit.Test;

public class AttributeTypeTest extends DomainTest {
	
	@Test
	public void attributeTypesCanBeFluentlyConstructed() {
		
		AttributeType a = attributeType().name(name).build();
		
		assertEquals(name,a.name());
		assertEquals(DEFAULT_TYPE,a.type());
		assertNull(a.language());
		assertEquals(text(),a.valueType());
		assertEquals(arbitrarily,a.range());
		
		a = attributeType().name(name).ofType(type).build();
		
		assertEquals(type,a.type());
		
		a = attributeType().name(name).ofType(type).in(language).build();
		
		assertEquals(language,a.language());
		
		a = attributeType().name(name).ofType(type).valuesIs(text().length(20)).in(language).build();
		
		assertEquals(text().length(20),a.valueType());
		
		a = attributeType().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build();
		
		assertEquals(once,a.range());

	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		AttributeType a;
		
		//modified attributes
		 a = modifyAttributeType("1").name(name).build();
		 a = modifyAttributeType("1").ofType(type).build();
		 a = modifyAttributeType("1").in(language).build();
		 a = modifyAttributeType("1").valuesIs(text()).build();
		 a = modifyAttributeType("1").occurs(once).build();
		 
		assertTrue(reveal(a).isChangeset());
		assertEquals(MODIFIED,reveal(a).status());		
		
		//removed attributes
		a = deleteAttributeType("1");

		assertTrue(reveal(a).isChangeset());
		assertEquals(DELETED,reveal(a).status());		
	}


	@Test
	public void cloned() {
		
		AttributeType a = like(attributeType().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		AttributeType.State state = reveal(a).state();
		AttributeTypeMS clone = new AttributeTypeMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void emptyChangeset() {

		AttributeType a = like(attributeType().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		AttributeType changeset = modifyAttributeType(a.id()).build();
		
		reveal(a).update(reveal(changeset)); 

		assertEquals(name, a.name());
		assertEquals(text().length(20), a.valueType());
		assertEquals(type, a.type());
		assertEquals(language, a.language());
		assertEquals(once, a.range());
	}
	
	
	
	@Test
	public void changesAttributes() {

		AttributeType a = like(attributeType().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		AttributeType changeset = modifyAttributeType(a.id()).name(name2).ofType(type2).valuesIs(text().length(10)).in(language2).occurs(arbitrarily).build();
		
		reveal(a).update(reveal(changeset));

		assertEquals(name2, a.name());
		assertEquals(text().length(10), a.valueType());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
		assertEquals(arbitrarily, a.range());
	}
	
	

	@Test
	public void cannotErasetName() {

		
		AttributeType a = like(attributeType().name(name).build());
		AttributeType changeset = modifyAttributeType(a.id()).name(NULL_QNAME).build();
		
		try {
			reveal(a).update(reveal(changeset));
			fail();
		}
		catch(IllegalArgumentException e) {}

	}

	
	@Test
	public void erasesType() {

		AttributeType a = like(attributeType().name(name).ofType(type).build());
	
		AttributeType changeset = modifyAttributeType(a.id()).ofType(NULL_QNAME).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.type());
	}

	
	@Test
	public void erasesLanguage() {

		AttributeType a = like(attributeType().name(name).in(language).build());
	
		AttributeType changeset = modifyAttributeType(a.id()).in(NULL_STRING).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.language());
	}
}
