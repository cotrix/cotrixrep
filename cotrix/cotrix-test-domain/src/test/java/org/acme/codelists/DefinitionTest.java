package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.attributes.Text.*;
import static org.cotrix.domain.common.OccurrenceRanges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.memory.DefinitionMS;
import org.junit.Before;
import org.junit.Test;

public class DefinitionTest extends DomainTest {
	
	Definition def = definition().name(name).is(type).valueIs(text().max(20)).in(language).occurs(once).build();
	
	@Before
	public void before() {
		
		def = like(def);
	}
	
	@Test
	public void canBeFluentlyConstructed() {
	
		Definition minimal = definition().name(name).build();
		
		//defaults
		
		assertEquals(defaultType,minimal.type());
		assertEquals(freetext,minimal.valueType());
		assertEquals(arbitrarily,minimal.range());
		assertNull(minimal.language());
		
		
		//a maximal sentence
		
		assertEquals(name,def.name());
		assertEquals(type,def.type());
		assertEquals(language,def.language());
		assertEquals(text().max(20),def.valueType());
		assertEquals(once,def.range());

	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		Definition a;
		
		 //change name
		 a = modify(def).name(name).build();
		
		 //change type
		 a = modify(def).is(type).build();
		 
		//change language
		 a = modify(def).in(language).build();
		 
		//change value type
		 a = modify(def).valueIs(text()).build();
		 
		//change occurrence constraints
		 a = modify(def).occurs(once).build();
		 
		
		assertTrue(reveal(a).isChangeset());
		
		assertEquals(MODIFIED,reveal(a).status());		
		
		a = delete(def);

		assertTrue(reveal(a).isChangeset());
		assertEquals(DELETED,reveal(a).status());		
	}


	@Test
	public void canBeCloned() {
		
		Definition.State state = reveal(def).state();
		DefinitionMS clone = new DefinitionMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void canBeUpdated() {

		Definition changeset = modify(def).name(name2).is(type2).valueIs(text().max(10)).in(language2).occurs(arbitrarily).build();
		
		reveal(def).update(reveal(changeset));

		assertEquals(changeset, def);
	}
	
	

	@Test(expected=IllegalArgumentException.class)
	public void cannotErasetName() {

		Definition changeset = modify(def).name(NULL_QNAME).build();
		
		reveal(def).update(reveal(changeset));
		
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void cannotEraseType() {

		Definition changeset = modify(def).is(NULL_QNAME).build();
		
		reveal(def).update(reveal(changeset));
	}

	
	@Test
	public void canEraseLanguage() {

		Definition changeset = modify(def).in(NULL_STRING).build();
		
		reveal(def).update(reveal(changeset));
		
		assertNull(def.language());
	}
}
