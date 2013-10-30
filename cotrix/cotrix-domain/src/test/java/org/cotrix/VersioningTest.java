package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Container;
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
	public void containersCanBeCopied() {
		
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private) attr().name(name2).value(value2).build();
		
		Container.Private<Attribute.Private> container = container(a,a2);
		
		Container.Private<Attribute.Private> clone = container.copy();
		
		System.out.println(container);
		assertEquals(container,clone);
		
		//copy is recursive
		assertNotSame(asMap(container).get(name).get(0), asMap(clone).get(name).get(0));

	}

	
	@Test
	public void codesCanBeCopied() {

		Attribute a = attr().name(name).value(value).build();
		Attribute a2 = attr().name(name2).value(value2).build();
				
		Code.Private code = (Code.Private) code().name(name).attributes(a,a2).build();
		
		Code.Private copy = code.copy();
		
		System.out.println(copy);
		assertEquals(code,copy);
		
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
	
	@Test
	public void codebagsCanBeVersioned() {

		Codelist.Private list = (Codelist.Private) codelist("id").name(name).build();
		
		Codebag.Private bag = (Codebag.Private) codebag("1").name(name).with(list).version(version).build(); 
				
		//a new version of the list
		Codebag versioned = bag.bump("2");
				
		assertEquals("2",versioned.version());
				
		//the new version is a fork
		assertFalse(bag.equals(versioned));
		
		assertFalse(bag.lists().equals(versioned.lists()));
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void versionsMustBeConsistent() {
		
		Codelist.Private list = (Codelist.Private) codelist("1").name(name).version("2").build(); 
		
		//a failed attempt to version the list with an unacceptable number
		list.bump("1.3");
		
	}
	
	
	

}
