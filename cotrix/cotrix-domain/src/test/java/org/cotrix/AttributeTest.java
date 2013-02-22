package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Factory;
import org.cotrix.domain.common.Delta;
import org.cotrix.domain.pos.AttributePO;
import org.cotrix.domain.simple.SimpleFactory;
import org.cotrix.domain.simple.SimpleLanguageAttribute;
import org.cotrix.domain.utils.IdGenerator;
import org.junit.Test;

public class AttributeTest {

	static Factory factory = new SimpleFactory();

	//NOTE: we test DOs independently from DSL and use utility methods to construct POs and make our tests less verbose
	//NOTE: we test language attributes that subsume behaviour of normal attributes
	
	@Test
	public void attributesCanBeConstructed() {
			
		SimpleLanguageAttribute a = new SimpleLanguageAttribute(po("id",name,type,value, language));
		
		assertEquals("id",a.id());
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(type,a.type());
		assertEquals(language,a.language());
		
	}
	
	@Test
	public void badAttributesCannotBeConstructed() {
			
		//validation is performed in POs, across DO implementations
		
		try {//null name
			po("id",null,type,value,language);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {//null value
			po(null,name,type,null,language);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {//null language
			po(null,name,type,value,null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		//type is defaulted, but cannot be set to null. verbose to test here, skip
			
	}
	
	@Test
	public void attributesCanBeCopied() {
		
		final String id = "id";
		IdGenerator generator = new IdGenerator() {
			
			@Override
			public String generateId() {
				return id;
			}
		};
		
		AttributePO po = po(id,name,type,value,language);
		
		SimpleLanguageAttribute a = new SimpleLanguageAttribute(po);
		
		SimpleLanguageAttribute copy = a.copy(generator);
		
		assertEquals(a,copy);

	}
	
	@Test
	public void attributesCanBeUpdated() {
		
		String id = "id";
		QName updated = new QName("updated");
		
		AttributePO po  = po(id,name,type,value,language);
		SimpleLanguageAttribute a = new SimpleLanguageAttribute(po);
		
		AttributePO poDelta =  po(id,updated,updated,"updated","updated");
		poDelta.setDelta(Delta.CHANGED);
		
		SimpleLanguageAttribute delta = new SimpleLanguageAttribute(poDelta);
		
		a.update(delta);
		
		assertEquals(updated,a.name());
		assertEquals("updated",a.value());
		assertEquals(updated,a.type());
		assertEquals("updated",a.language());
	}
	
	@Test
	public void onlyNewUnidentifiedAttributesCanBeNewOrDeleted() {
		
		//we do it for attributes, but then applies to all objects that delegate to super...
		AttributePO po  = po(null,name,type,value,language);
		
		//only new 
		po.setDelta(Delta.NEW);
		
		try {
			po.setDelta(Delta.CHANGED);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			po.setDelta(Delta.DELETED);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		
	}
	
	@Test
	public void attributeAndDeltasMustBeValidForUpdate() {
		
		//we do it for attributes, but then applies to all objects that delegate to super...
		
		String id = "id";
		QName updated = new QName("updated");
		
		AttributePO po  = po(id,name,type,value,language);
		
		Attribute a = new SimpleLanguageAttribute(po);
		
		
		AttributePO poDelta =  po(id,updated,updated,"updated","updated");
		SimpleLanguageAttribute delta = new SimpleLanguageAttribute(poDelta);
		
		//not a delta object
		try {
			a.update(delta);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		
		//not a CHANGED object
		poDelta.setDelta(Delta.DELETED);
		delta = new SimpleLanguageAttribute(poDelta);
		
		try {
			a.update(delta);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		//identifier mismatch
		poDelta =  po("badid",updated,updated,"updated","updated");
		poDelta.setDelta(Delta.DELETED);
		delta = new SimpleLanguageAttribute(poDelta);
		
		try {
			a.update(delta);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
	
	//helper
	private AttributePO po(String id,QName name,QName type, String value, String language) {
		
		AttributePO po = new AttributePO(id);
		po.setName(name);
		po.setType(type);
		po.setValue(value);
		po.setLanguage(language);
		
		return po;
	}

}
