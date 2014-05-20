package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.links.ValueFunctions.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.CodelistMS;
import org.junit.Test;

public class CodelistTest extends DomainTest {

	@Test
	public void codelistCanBeFluentlyConstructed() {
		
		Codelist list  = codelist().name(name).build();
		
		assertEquals(name,list.name());
		
		Attribute a = attribute().name(name).build();
		
		list= codelist().name(name).attributes(a).build();
		
		assertTrue(list.attributes().contains(a));
		
		Code c = code().name(name).build();
		
		list = codelist().name(name).with(c).build();
		
		assertTrue(list.codes().contains(c));
		
		list = codelist().name(name).version("1.0").build();
		
		assertEquals("1.0",list.version());
		
		CodelistLink link = listLink().name(name).target(list).anchorToName().build();
		
		list = codelist().name(name).links(link).build();
		
		assertTrue(list.links().contains(link));
		
		//other correct sentences
		codelist().name(name).attributes(a).version("1").build();
		codelist().name(name).with(c).version("1").build();
		codelist().name(name).with(c).attributes(a).build();
		codelist().name(name).with(c).attributes(a).version("1").build();
		
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Code c = code().name("name").build();
		Attribute a = attribute().name(name).build();
		
		Codelist list;
		
		list = modifyCodelist("1").name(name).build();
		list =  modifyCodelist("1").attributes(a).build();
		list =  modifyCodelist("1").with(c).build();
		list =  modifyCodelist("1").with(c).attributes(a).build();	
		
		CodelistLink link = listLink().name(name).target(codelist().name(name).build()).anchorToName().build();
		
		list =  modifyCodelist("1").links(link).build();
		
		//changed
		assertEquals(MODIFIED,((Codelist.Private) list).status());

	}
	
	@Test
	public void cloned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		Code c = code().name(name).attributes(a).build();
		Codelist list = like(codelist().name(name).with(c).build());
		
		Codelist.State state = reveal(list).state();
		CodelistMS clone = new CodelistMS(state);
		
		assertFalse(clone.id().equals(state.id()));
		
	}
	
	@Test
	public void versioned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		Code c = code().name(name).attributes(a).build();
		
		Codelist list = like(codelist().name(name).with(c).version("1").build());
		
		Codelist versioned = reveal(list).bump("2");

		assertEquals(versioned.attributes().lookup(PREVIOUS_VERSION_NAME).value(),list.name().toString());
		assertEquals(versioned.attributes().lookup(PREVIOUS_VERSION_ID).value(),list.id());
		assertEquals(versioned.attributes().lookup(PREVIOUS_VERSION).value(),list.version());
		
		assertEquals(versioned.codes().lookup(c.name()).attributes().lookup(PREVIOUS_VERSION_ID).value(),c.id());
		assertEquals(versioned.codes().lookup(c.name()).attributes().lookup(PREVIOUS_VERSION_NAME).value(),c.name().toString());

		assertEquals("2",versioned.version());
		
		System.out.println(versioned);
		
	}
	
	
	@Test
	public void codelistWithLinksCanBeVersioned() {
		
		
		Code ctarget = code().name("a").build();
		Codelist target = like(codelist().name("A").with(ctarget).build());  
		
		
		CodelistLink listLink = listLink().name("link").target(target).transformWith(lowercase).anchorToName().build();
		Codelink link  = link().instanceOf(listLink).target(ctarget).build();
		Code csource = code().name("b").links(link).build();
		
		Codelist source = codelist().name("B").links(listLink).with(csource).build();
		
		source = like(source);
		
		Codelist versioned = reveal(source).bump("2.0");

		like(versioned);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void versionsMustBeConsistent() {
		
		Codelist list = like(codelist().name(name).version("2").build()); 
		
		//a failed attempt to version the list with an unacceptable number
		reveal(list).bump("1.3");
		
	}
	
	@Test
	public void changeCode() {
		
		Codelist list = like(codelist().name(name).build());
		
		Codelist changeset = modifyCodelist(list.id()).name(name2).build();
		
		reveal(list).update(reveal(changeset));
		
		assertEquals(name2,list.name());
		
	}
	
	@Test
	public void addCode() {
		
		Codelist list = like(codelist().name(name).build());


		Code added = code().name(name).build();
		
		assertFalse(list.codes().contains(added));

		Codelist changeset = modifyCodelist(list.id()).with(added).build();
		
		reveal(list).update(reveal(changeset));
		
		assertTrue(list.codes().contains(name));
		
	}
	
	@Test
	public void removeCode() {
		
		Code c = code().name(name).build();
		
		Codelist list = like(codelist().name(name).with(c).build());
		
		assertTrue(list.codes().contains(c));

		Code deleted = deleteCode(c.id());
		
		Codelist changeset = modifyCodelist(list.id()).with(deleted).build();
		
		reveal(list).update(reveal(changeset));
		
		assertFalse(list.codes().contains(c));
		
	}


}
