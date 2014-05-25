package org.acme.codelists;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.validation.Validators.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.ValueType;
import org.junit.Test;

public class ValueTypeTest extends DomainTest {
	
	ValueType type = valueType()
							.with(min_length.instance("2"),max_length.instance("3"))
							.required();
	
	@Test
	public void canBeFluentlyConstructed() {
	
		ValueType minimal = valueType();
		
		//defaults
		
		assertNotNull(minimal.constraint());
		assertFalse(minimal.isRequired());
		assertTrue(minimal.isValid("anything"));
		
		
		//a maximal sentence
		
		assertNotNull(type.constraint());
		assertTrue(type.isRequired());
		assertFalse(type.isValid("1"));
		assertTrue(type.isValid("12"));
		assertTrue(type.isValid("123"));

	}
}
