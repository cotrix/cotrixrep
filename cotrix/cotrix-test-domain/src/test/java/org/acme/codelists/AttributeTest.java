package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.memory.AttributeMS;
import org.cotrix.domain.values.ValueType;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest extends DomainTest {

	Attribute untyped = attribute().name(name).ofType(type).value(value).in(language).description(description).build();

	ValueType valuetype = valueType().defaultsTo("mydef");
	
	Definition def = definition().name(name).build();
	
	Attribute typed = attribute().with(def).value(value).build();

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
		assertNull(minimalUntyped.value());
		assertNull(minimalUntyped.description());

		// full untyped: delegates
		assertEquals(untyped.definition().qname(), untyped.qname());
		assertEquals(untyped.definition().type(), untyped.type());
		assertEquals(untyped.definition().language(), untyped.language());

		// defaults, typed

		Definition def = definition().name(name).valueIs(valuetype).build();
		Attribute minimalTyped = attribute().with(def).build();

		assertEquals(def, minimalTyped.definition());
		assertEquals(valuetype.defaultValue(), minimalTyped.value());
		
		

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
		
		//change description
		modify(untyped).description(description).build();
	
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
		
		Attribute changesetUntyped = modify(untyped).name(newname).value(newvalue).ofType(newtype).in(newlanguage).description(newdescription).build();

		reveal(untyped).update(reveal(changesetUntyped));

		assertEquals(changesetUntyped.qname(), untyped.qname());
		assertEquals(changesetUntyped.value(), untyped.value());
		assertEquals(changesetUntyped.type(), untyped.type());
		assertEquals(changesetUntyped.language(), untyped.language());
		assertEquals(changesetUntyped.description(), untyped.description());
		
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
	public void canEraseDescription() {

		Attribute changeset = modify(typed).description(NULL_STRING).build();

		reveal(typed).update(reveal(changeset));

		assertNull(typed.description());
	}

	@Test(expected=IllegalArgumentException.class)
	public void canEraseType() {

		Attribute changeset = modify(untyped).ofType(NULL_QNAME).build();

		reveal(untyped).update(reveal(changeset));
	}

	@Test
	public void canEraseLanguage() {

		Attribute changeset = modify(untyped).in(NULL_STRING).build();

		reveal(untyped).update(reveal(changeset));

		assertNull(untyped.language());
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void cannoteViolateConstraints() {

		//definition becomes more restrictive
		ValueType newtype = valueType().with(max_length.instance(2));

		Definition defChangeset = modify(typed.definition()).valueIs(newtype).build();

		reveal(typed.definition()).update(reveal(defChangeset));

		//conforming values are accepted
		Attribute changeset = modify(typed).value("12").build();
		
		reveal(typed).update(reveal(changeset));
		
		//violations are detected
		changeset = modify(typed).value("123").build();
		
		reveal(typed).update(reveal(changeset));
	}
}
