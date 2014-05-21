package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.common.OccurrenceRanges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.memory.DefinitionMS;
import org.junit.Test;

public class DefinitionTest extends DomainTest {
	
	@Test
	public void attributeTypesCanBeFluentlyConstructed() {
		
		Definition a = definition().name(name).build();
		
		assertEquals(name,a.name());
		assertEquals(DEFAULT_TYPE,a.type());
		assertNull(a.language());
		assertEquals(text(),a.valueType());
		assertEquals(arbitrarily,a.range());
		
		a = definition().name(name).ofType(type).build();
		
		assertEquals(type,a.type());
		
		a = definition().name(name).ofType(type).in(language).build();
		
		assertEquals(language,a.language());
		
		a = definition().name(name).ofType(type).valuesIs(text().length(20)).in(language).build();
		
		assertEquals(text().length(20),a.valueType());
		
		a = definition().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build();
		
		assertEquals(once,a.range());

	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		Definition a;
		
		//modified attributes
		 a = modifyDefinition("1").name(name).build();
		 a = modifyDefinition("1").ofType(type).build();
		 a = modifyDefinition("1").in(language).build();
		 a = modifyDefinition("1").valuesIs(text()).build();
		 a = modifyDefinition("1").occurs(once).build();
		 
		assertTrue(reveal(a).isChangeset());
		assertEquals(MODIFIED,reveal(a).status());		
		
		//removed attributes
		a = deleteDefinition("1");

		assertTrue(reveal(a).isChangeset());
		assertEquals(DELETED,reveal(a).status());		
	}


	@Test
	public void cloned() {
		
		Definition a = like(definition().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		Definition.State state = reveal(a).state();
		DefinitionMS clone = new DefinitionMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void emptyChangeset() {

		Definition a = like(definition().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		Definition changeset = modifyDefinition(a.id()).build();
		
		reveal(a).update(reveal(changeset)); 

		assertEquals(name, a.name());
		assertEquals(text().length(20), a.valueType());
		assertEquals(type, a.type());
		assertEquals(language, a.language());
		assertEquals(once, a.range());
	}
	
	
	
	@Test
	public void changesAttributes() {

		Definition a = like(definition().name(name).ofType(type).valuesIs(text().length(20)).in(language).occurs(once).build());
		
		Definition changeset = modifyDefinition(a.id()).name(name2).ofType(type2).valuesIs(text().length(10)).in(language2).occurs(arbitrarily).build();
		
		reveal(a).update(reveal(changeset));

		assertEquals(name2, a.name());
		assertEquals(text().length(10), a.valueType());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
		assertEquals(arbitrarily, a.range());
	}
	
	

	@Test
	public void cannotErasetName() {

		
		Definition a = like(definition().name(name).build());
		Definition changeset = modifyDefinition(a.id()).name(NULL_QNAME).build();
		
		try {
			reveal(a).update(reveal(changeset));
			fail();
		}
		catch(IllegalArgumentException e) {}

	}

	
	@Test
	public void erasesType() {

		Definition a = like(definition().name(name).ofType(type).build());
	
		Definition changeset = modifyDefinition(a.id()).ofType(NULL_QNAME).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.type());
	}

	
	@Test
	public void erasesLanguage() {

		Definition a = like(definition().name(name).in(language).build());
	
		Definition changeset = modifyDefinition(a.id()).in(NULL_STRING).build();
		
		reveal(a).update(reveal(changeset));
		
		assertNull(a.language());
	}
}
