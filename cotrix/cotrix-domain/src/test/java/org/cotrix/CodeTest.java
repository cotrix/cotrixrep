package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Code;
import org.cotrix.domain.common.Bag;
import org.junit.Test;

public class CodeTest {

	@Test
	public void codesAreCorrectlyConstructed() {
			
		Code code = new Code(name); 
				
		assertEquals(0,code.attributes().size());
		assertEquals(name,code.name());
		
	}
	
	@Test
	public void codesAreCorrectlyConstructedWithAttributes() {
			
		Bag<Attribute> attributes = new Bag<Attribute>();
		
		attributes.add(new Attribute(name,value));
		
		Code code = new Code(name,attributes); 
		
		assertEquals(attributes, code.attributes());
		
	}
	
	@Test
	public void codesAreCorrectlyCloned() {
			
		Bag<Attribute> attributes = new Bag<Attribute>();
		
		attributes.add(new Attribute(name,value));
		
		Code code = new Code(name,attributes); 
		
		Code clone  = code.copy();
		
		assertEquals(code,clone);
		
	}
	

}
