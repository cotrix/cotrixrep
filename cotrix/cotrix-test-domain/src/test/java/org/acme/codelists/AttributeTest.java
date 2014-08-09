package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.memory.MAttrDef;
import org.cotrix.domain.memory.MAttribute;
import org.cotrix.domain.values.ValueType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AttributeTest extends DomainTest {

	Attribute untyped = attribute().name(name).ofType(type).value(value).in(language).description(description).build();

	ValueType valuetype = valueType().defaultsTo("mydef");
	
	AttributeDefinition def = attrdef().name(name).build();
	
	Attribute typed = attribute().instanceOf(def).value(value).build();
	Attribute typed2 = attribute().instanceOf(def).value(value2).build();

	@Before
	public void before() {

		untyped = like(untyped);
		typed = like(typed);
		typed2 = like(typed2);
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

		AttributeDefinition def = attrdef().name(name).valueIs(valuetype).build();
		Attribute minimalTyped = attribute().instanceOf(def).build();

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
	
		AttributeDefinition newdef = attrdef().name(name).build();

		//change definition
		modify(typed).instanceOf(newdef).build();
		
		//change definition and value
		modifyAttribute("1").instanceOf(newdef).value(value).build();
	}

	
	@Test
	public void canBeCloned() {

		Attribute.Bean state = reveal(untyped).bean();
	
		MAttribute clone = new MAttribute(state);
		
		assertEquals(clone, state);

		//clones preserve identifiers
		assertEquals(state.id(),clone.id());
		
		//but not definitions
		assertNotEquals(state.definition().id(),clone.definition().id());
	}

	
	@Test
	public void cloningCanPreserveSharing() {
		
		//context to preserve sharing
		Map<String,Object> ctx = new HashMap<>();
		
		ctx.put(typed.definition().id(), new MAttrDef(beanOf(typed.definition())));
		
		MAttribute clone1 = new MAttribute(beanOf(typed),ctx);
		MAttribute clone2 = new MAttribute(beanOf(typed2),ctx);
		
		assertSame(clone1.definition(),clone2.definition());
		
		
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
		
		AttributeDefinition newdef = attrdef().name(name2).build();

		Attribute changesetTyped = modify(typed).instanceOf(newdef).value(value2).build();

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
	
	
	@Ignore @Test(expected=IllegalArgumentException.class)
	public void cannoteViolateConstraints() {

		//definition becomes more restrictive
		ValueType newtype = valueType().with(max_length.instance(2));

		AttributeDefinition defChangeset = modify(typed.definition()).valueIs(newtype).build();

		reveal(typed.definition()).update(reveal(defChangeset));

		//conforming values are accepted
		Attribute changeset = modify(typed).value("12").build();
		
		reveal(typed).update(reveal(changeset));
		
		//violations are detected
		changeset = modify(typed).value("123").build();
		
		reveal(typed).update(reveal(changeset));
	}
}
