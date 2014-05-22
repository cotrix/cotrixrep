package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.memory.AttributeMS;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest extends DomainTest {

	Attribute untyped = attribute().name(name).ofType(type).value(value).in(language).build();

	Attribute typed = attribute().with(definition().name(name).build()).value(value).build();

	@Before
	public void before() {

		untyped = like(untyped);
		typed = like(typed);
	}

	@Test
	public void attributesCanBeFluentlyConstructed() {

		Attribute minimalUntyped = attribute().name(name).build();

		// defaults, untyped
		assertNotNull(minimalUntyped.definition()); // private def
		assertEquals(null, minimalUntyped.value());

		// full untyped: delegates
		assertEquals(untyped.definition().name(), untyped.name());
		assertEquals(untyped.definition().type(), untyped.type());
		assertEquals(untyped.definition().language(), untyped.language());

		// defaults, typed

		Definition def = definition().name(name).build();
		Attribute minimalTyped = attribute().with(def).build();

		assertEquals(def, minimalTyped.definition());
		assertEquals(null, minimalTyped.value());

	}

	@Test
	public void changesetsCanBeFluentlyConstructed() {

		//change name
		modify(untyped).name(name).build();
		
		//change value
		modify(untyped).value(value).build();
		
		//change type
		modify(untyped).ofType(type).build();
		
		//change language
		modify(untyped).in(language).build();
	
		Definition newdef = definition().name(name).build();

		//change definition
		modify(typed).with(newdef).build();
		
		//change definition and value
		modifyAttribute("1").with(newdef).value(value).build();
	}

	
	@Test
	public void canBeCloned() {

		//clone untyped
		Attribute.State state = reveal(untyped).state();
		AttributeMS clone = new AttributeMS(state);

		assertEquals(clone, state);
		
		//clone typed
		state = reveal(typed).state();
		clone = new AttributeMS(state);

		assertEquals(clone, state);

	}

	@Test
	public void canBeUpdated() {

		//untyped
		
		Attribute changesetUntyped = modify(untyped).name(name2).value(value2).ofType(type2).in(language2).build();

		reveal(untyped).update(reveal(changesetUntyped));

		assertEquals(changesetUntyped.name(), untyped.name());
		assertEquals(changesetUntyped.value(), untyped.value());
		assertEquals(changesetUntyped.type(), untyped.type());
		assertEquals(changesetUntyped.language(), untyped.language());
		
		//typed
		
		Definition newdef = definition().name(name2).build();

		Attribute changesetTyped = modify(typed).with(newdef).value(value2).build();

		reveal(typed).update(reveal(changesetTyped));

		assertEquals(changesetTyped.definition(),typed.definition());
		assertEquals(changesetTyped.value(), typed.value());
	}

	@Test(expected=IllegalArgumentException.class)
	public void cannotErasetName() {

		Attribute changeset = modify(untyped).name(NULL_QNAME).build();

		reveal(untyped).update(reveal(changeset));
	
	}

	@Test
	public void canEraseValue() {

		Attribute changeset = modify(typed).value(NULL_STRING).build();

		reveal(typed).update(reveal(changeset));

		assertNull(typed.value());
	}

	@Test
	public void canEraseType() {

		Attribute changeset = modify(untyped).ofType(NULL_QNAME).build();

		reveal(untyped).update(reveal(changeset));

		assertNull(untyped.type());
	}

	@Test
	public void canEraseLanguage() {

		Attribute changeset = modify(untyped).in(NULL_STRING).build();

		reveal(untyped).update(reveal(changeset));

		assertNull(untyped.language());
	}
}
