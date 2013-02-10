package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.LanguageAttribute;
import org.junit.Test;

public class AttributeTest {

	@Test
	public void attributesAreCorrectlyConstructed() {
			
		Attribute attribute = new Attribute(name,value);
		
		assertEquals(name,attribute.name());
		assertEquals(value,attribute.value());
		assertNull(attribute.type());
		
		attribute = new Attribute(name,type,value);
		
		assertEquals(type,attribute.type());
		
	}
	
	@Test
	public void localeAttributesAreCorrectlyConstructed() {
			
		LanguageAttribute attribute = new LanguageAttribute(name,value,language);
		
		assertEquals(name,attribute.name());
		assertEquals(value,attribute.value());
		assertEquals(language,attribute.language());
		assertNull(attribute.type());
		
		attribute = new LanguageAttribute(name,type,value,language);
		
		assertEquals(type,attribute.type());
		
	}
	
	@Test
	public void attributesAreCorrectlyCloned() {
		
		Attribute attribute = new Attribute(name,type,value);
	
		Attribute clone = attribute.copy();
		
		assertEquals(attribute,clone);

	}
	
	@Test
	public void localeAttributesAreCorrectlyCloned() {
		
		LanguageAttribute attribute = new LanguageAttribute(name,type,value,language);
	
		LanguageAttribute clone = attribute.copy();
		
		assertEquals(attribute,clone);

	}
	
	@Test
	public void attributesBeCustomised() {
		
		Attribute attribute = new CustomAttribute(value);
		
		assertEquals(CustomAttribute.name, attribute.name());
		assertEquals(CustomAttribute.type, attribute.type());
		assertEquals(value, attribute.value());

	}

	
	static class CustomAttribute extends Attribute {
		
		private static final QName name = new QName("http://acme/org","custom");
		private static final QName type = new QName("http://acme/org","customtype");
		
		public CustomAttribute(String value) {
			super(name,type,value);
		}
	}
}
