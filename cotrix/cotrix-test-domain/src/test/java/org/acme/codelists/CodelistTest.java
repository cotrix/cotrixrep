package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCodelist.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.managed.ManagedCodelist;
import org.cotrix.domain.memory.CodelistMS;
import org.junit.Before;
import org.junit.Test;

public class CodelistTest extends DomainTest {
	
	Attribute attr = attribute().name(name).build();
	AttributeDefinition def = definition().name(name).build();
	Code code = code().name(name).build();
	Codelist target = codelist().name(name).build();
	LinkDefinition link = listLink().name(name).target(target).build();
	String version = "0.1";
	
	Codelist list = codelist()
					.name(name)
					.definitions(def)
					.attributes(attr)
					.with(code)
					.links(link)
					.version(version)
					.build();

	
	@Before
	public void stage() {
		
		target = like(target);
		list = like(list);
	}
	
	@Test
	public void codelistCanBeFluentlyConstructed() {
		
		
		//defaults
		Codelist minimal  = codelist().name(name).build();
		
		assertNotNull(minimal.version());
		assertNotNull(minimal.definitions());
		assertNotNull(minimal.attributes());
		assertNotNull(minimal.links());
		assertNotNull(minimal.codes());
		
		//full-fledged
		
		assertTrue(list.attributes().contains(attr));
		assertTrue(list.codes().contains(code));
		assertTrue(list.links().contains(link));
		assertTrue(list.definitions().contains(def));
		
		assertEquals(version,list.version());
		
		
		
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		//change definitions
		modify(list).definitions(def).build();
		
		//change attributes
		modify(list).attributes(attr).build();
		
		//change codes
		modify(list).with(code).build();
		
		//change links
		modify(list).links(link).build();
		
	}
	
	@Test
	public void canBeCloned() {
		
		Codelist.State state = reveal(list).state();

		//can simply check for equals at this stage
		CodelistMS clone = new CodelistMS(state);
		
		assertTrue(clone.attributes().contains(attr));
	}
	
	@Test
	public void canBeVersioned() {
		
		Codelist versioned = reveal(list).bump("2");

		assertEquals("2",versioned.version());

		ManagedCodelist managed = manage(versioned);
		
		//lineage is preserved:
		
		assertEquals(list.qname(),managed.originName());
		assertEquals(list.id(),managed.originId());
		assertEquals(list.version(),managed.previousVersion());
		
		ManagedCode managedCode = ManagedCode.manage(versioned.codes().lookup(code.qname()));
				
		assertEquals(code.id(), managedCode.originId());
		assertEquals(code.qname(),managedCode.originName());
		
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void versionsMustBeConsistent() {
		
		Codelist list = like(codelist().name(name).version("2").build()); 
		
		reveal(list).bump("1.3");
		
	}
	

	@Test
	public void canBeUpdated() {
		
		//don't need to re-test containers here
		//the simplest proof that update reaches them will do

		AttributeDefinition newdef = modify(def).name("newname").build();
		LinkDefinition newlink = modify(link).name("newname").build();
		
		Codelist changeset = modify(list)
				.name(name2)
				.definitions(newdef)
				.attributes(delete(attr)) 
				.with(delete(code))
				.links(newlink)
				.build();
		
		reveal(list).update(reveal(changeset));
		
		assertEquals(changeset.qname(),list.qname());
		
		assertTrue(list.definitions().contains(newdef.qname()));
		assertFalse(list.attributes().contains(attr.qname()));
		assertTrue(list.links().contains(newlink.qname()));
		assertFalse(list.codes().contains(code.qname()));
		
	}

}
