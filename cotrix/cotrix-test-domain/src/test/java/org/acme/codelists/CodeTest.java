package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.memory.CodeMS;
import org.junit.Before;
import org.junit.Test;

public class CodeTest extends DomainTest {

	Code targetcode = code().name(name2).build();
	Codelist target = codelist().name(name).with(targetcode).build();

	
	Attribute attr = attribute().name(name).build();
	CodelistLink listlink = listLink().name(name).target(target).build();
	Codelink link = link().instanceOf(listlink).target(targetcode).build();
	
	Code code = code()
					.name(name)
					.attributes(attr)
					.links(link)
					.build();
	
	@Before
	public void stage() {
		
		target = like(target);
		listlink = like(listlink);
		code = like(code);
	}
	
	@Test
	public void codesCanBeFluentlyConstructed() {
		
		//minimal
		Code minimal = code().name(name).build();
		
		assertNotNull(minimal.attributes());
		assertNotNull(minimal.links());
		
		//full
		
		assertTrue(code.attributes().contains(attr));
		assertTrue(code.links().contains(link));
		assertTrue(code.attributes().contains(attr));
	}
	
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {
		
		//change links
		modify(code).name(name).build();
		
		//change attributes
		modify(code).attributes(attr).build();
		
		//change links
		modify(code).links(link).build();
				
		delete(code);
	}
	
	
	@Test
	public void canBeCloned() {
		
		Code.State state = reveal(code).state();
		CodeMS clone = new CodeMS(state);
		
		assertEquals(state.name(),clone.name());
		assertTrue(clone.attributes().contains(attr.name()));
		assertTrue(clone.links().contains(link.name()));
		
		
	}
	
	@Test
	public void canBeUpdated() {
		
		//don't need to re-test containers here
		//the simplest proof that update reaches them will do
		
		Code changeset = modify(code)
							.name(name2)
							.attributes(delete(attr))
							.links(delete(link))
							.build();
		
		reveal(code).update(reveal(changeset));
		
		assertEquals(changeset.name(),code.name());
		
		assertFalse(code.attributes().contains(attr.name()));
		assertFalse(code.links().contains(link.name()));
		
	}
	
}
