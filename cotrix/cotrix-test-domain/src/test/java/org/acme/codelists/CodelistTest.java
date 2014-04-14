package org.acme.codelists;

import static org.junit.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;

import org.acme.DomainTest;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.version.DefaultVersion;
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
		
		assertEquals(clone,state);
		
		assertFalse(clone.id().equals(state.id()));
		
	}
	
	@Test
	public void versioned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		Code c = code().name(name).attributes(a).build();
		
		Codelist list = like(codelist().name(name).with(c).version("1").build());
		
		Codelist versioned = reveal(list).bump("2");
		
		assertEquals("2",versioned.version());
		
		Codelist.State state = reveal(list).state();
		state.version(new DefaultVersion("2"));
		CodelistMS clone = new CodelistMS(state);
		
		assertEquals(clone,state);
		
		assertFalse(clone.id().equals(state.id()));
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
