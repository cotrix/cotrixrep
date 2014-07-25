package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.memory.AttrDefinitionMS;
import org.cotrix.domain.values.ValueType;
import org.junit.Before;
import org.junit.Test;

public class DefinitionTest extends DomainTest {
	
	ValueType vtype = valueType().with(max_length.instance("20"));
	AttributeDefinition def = attrdef().name(name).is(type).valueIs(vtype).in(language).occurs(once).build();
	
	@Before
	public void before() {
		
		def = like(def);
	}
	
	@Test
	public void canBeFluentlyConstructed() {
	
		AttributeDefinition minimal = attrdef().name(name).build();
		
		//defaults
		
		assertEquals(DEFAULT_TYPE,minimal.type());
		assertEquals(defaultValueType,minimal.valueType());
		assertEquals(arbitrarily,minimal.range());
		assertNull(minimal.language());
		
		
		//a maximal sentence
		
		assertEquals(name,def.qname());
		assertEquals(type,def.type());
		assertEquals(language,def.language());
		assertEquals(valueType().with(max_length.instance("20")),def.valueType());
		assertEquals(once,def.range());

	}
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {

		AttributeDefinition a;
		
		 //change name
		 a = modify(def).name(name).build();
		
		 //change type
		 a = modify(def).is(type).build();
		 
		//change language
		 a = modify(def).in(language).build();
		 
		//change value type
		 a = modify(def).valueIs(defaultValueType).build();
		 
		//change occurrence constraints
		 a = modify(def).occurs(once).build();
		 
		
		assertTrue(reveal(a).isChangeset());
		
		assertEquals(MODIFIED,reveal(a).status());		
	}


	@Test
	public void canBeCloned() {
		
		AttributeDefinition.State state = reveal(def).state();
		AttrDefinitionMS clone = new AttrDefinitionMS(state);
		
		assertEquals(clone,state);
		
	}
	
	@Test
	public void canBeUpdated() {

		ValueType vtype2 = valueType().with(max_length.instance("10"));
		AttributeDefinition changeset = modify(def).name(name2).is(type2).valueIs(vtype2).in(language2).occurs(arbitrarily).build();
		
		reveal(def).update(reveal(changeset));

		assertEquals(changeset, def);
	}
	
	

	@Test(expected=IllegalArgumentException.class)
	public void cannotErasetName() {

		AttributeDefinition changeset = modify(def).name(NULL_QNAME).build();
		
		reveal(def).update(reveal(changeset));
		
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void cannotEraseType() {

		AttributeDefinition changeset = modify(def).is(NULL_QNAME).build();
		
		reveal(def).update(reveal(changeset));
	}

	
	@Test
	public void canEraseLanguage() {

		AttributeDefinition changeset = modify(def).in(NULL_STRING).build();
		
		reveal(def).update(reveal(changeset));
		
		assertNull(def.language());
	}
}
