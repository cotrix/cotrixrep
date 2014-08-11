package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;
import org.junit.Before;
import org.junit.Test;

public class CodelistTest extends DomainTest {
	
	Attribute attr = attribute().name(name).build();
	
	AttributeDefinition def = attrdef().name(name).build();
	Attribute shared = attribute().instanceOf(def).value(value).build();
	Attribute shared2 = attribute().instanceOf(def).value(value2).build();
	
	Code tcode1 = code().name(name).build();
	Code tcode2 = code().name(name2).build();
	Codelist target = codelist().name(name).with(tcode1,tcode2).build();
	
	LinkDefinition link = linkdef().name(name).target(target).build();
	
	Link link1 = link().instanceOf(link).target(tcode1).build();
	Link link2 = link().instanceOf(link).target(tcode2).build();
	
	Code code = code().name(name).attributes(shared,shared2).links(link1,link2).build();
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
		
		Codelist.Bean state = reveal(list).bean();

		//can simply check for equals at this stage
		MCodelist clone = new MCodelist(state);
		
		assertTrue(clone.attributes().contains(attr.qname()));
	}
	
	@Test
	public void canBeVersioned() {
		
		Codelist versioned = reveal(list).bump("2");

		assertEquals("2",versioned.version());
		
		//lineage is preserved:
		
		assertEquals(list.qname(),PREVIOUS_VERSION_NAME.nameOf(versioned));
		assertEquals(list.id(),PREVIOUS_VERSION_ID.of(versioned));
		assertEquals(list.version(),PREVIOUS_VERSION.of(versioned));
		
		Code vcode = versioned.codes().getFirst(code);
				
		assertEquals(code.id(), PREVIOUS_VERSION_ID.of(vcode));
		assertEquals(code.qname(),PREVIOUS_VERSION_NAME.nameOf(vcode));
		
	}
	
	@Test
	public void versioningPreservesAttributeSharing() {
		
		//same state to begin with
		assertSame(beanOf(shared.definition()),beanOf(shared2.definition()));
		
		Codelist versioned = reveal(list).bump("2");

		Code vcode = versioned.codes().getFirst(code);
		
		Attribute vshared = vcode.attributes().lookup(shared.id());
		Attribute vshared2 = vcode.attributes().lookup(shared2.id());
		
		//equivalent to original
		assertEquals(shared.definition(),vshared.definition());
		
		//but state is distinct
		assertNotSame(beanOf(shared.definition()),beanOf(vshared.definition()));

		//whilst state sharing is preserved
		assertSame(beanOf(vshared.definition()),beanOf(vshared2.definition()));
		
		
	}
	
	@Test
	public void versioningPreservesLinkSharing() {
		
		//same state to begin with
		assertSame(beanOf(link1.definition()),beanOf(link2.definition()));
		
		Codelist versioned = reveal(list).bump("2");

		Code vcode = versioned.codes().getFirst(code);
		
		Link vlink1 = vcode.links().lookup(link1.id());
		Link vlink2 = vcode.links().lookup(link2.id());

		//equivalent to original TODO: this is unstable
		//assertEquals(link1.definition(),vlink1.definition());
		
		//but state is distinct
		assertNotSame(beanOf(link1.definition()),beanOf(vlink1.definition()));
		
		//whilst state sharing is preserved
		assertSame(beanOf(vlink1.definition()),beanOf(vlink2.definition()));
		
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
		
		assertTrue(list.definitions().contains(newdef));
		assertFalse(list.attributes().contains(attr));
		assertTrue(list.links().contains(newlink));
		assertFalse(list.codes().contains(code));
		
	}

}
