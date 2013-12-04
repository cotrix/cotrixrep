package org.acme;

import static junit.framework.Assert.*;
import static org.acme.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.junit.Test;

public class VersioningTest {

	//we answer the question: can DOs be correctly versioned?
	//this applies to codebags and codelists but requires all DOs to yield copies
	//so we mostly answer the question: can DOs be correctly copied?
	
	
	@Test
	public void copyPreserveName() {
		
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).build();
		
		Attribute copy = a.copy();
		
		assertEquals(name,copy.name());
	}
	
	@Test
	public void copiesDoNotPreserveStatus() {
		
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).build();
		
		Attribute.Private copy = a.copy();
		
		assertNull(copy.status());
	}
	
	@Test
	public void attributesCanBeCopied() {
		
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).in(language).build();
		
		Attribute copy = a.copy();
		
		assertEquals(a,copy);
		
		
	}

	@Test
	public void codesCanBeCopied() {

		Attribute a = attr().name(name).value(value).build();
		Attribute a2 = attr().name(name2).value(value2).build();
				
		Code.Private code = (Code.Private) code().name(name).attributes(a,a2).build();
		
		Code.Private copy = code.copy();
		
		
		assertEquals(code.attributes(),copy.attributes());
		
		//copy is recusive
		assertNotSame(code.attributes(), copy.attributes());

	}
	
	@Test
	public void codelistsCanBeVersioned() {

		Code.Private code = (Code.Private) code("id").name(name).build();
		
		Codelist.Private list = (Codelist.Private) codelist("1").name(name).with(code).version(version).build(); 
				
		//a new version of the list
		Codelist versioned = list.bump("2");
				
		assertEquals("2",versioned.version());
		
		assertNull(versioned.id());
				
		assertFalse(list.equals(versioned));

		assertFalse(list.codes().equals(versioned.codes()));
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void versionsMustBeConsistent() {
		
		Codelist.Private list = (Codelist.Private) codelist("1").name(name).version("2").build(); 
		
		//a failed attempt to version the list with an unacceptable number
		list.bump("1.3");
		
	}
	
	
	

}
