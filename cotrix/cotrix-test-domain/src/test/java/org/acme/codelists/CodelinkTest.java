package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.acme.DomainTest;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.CodelinkMS;
import org.junit.Test;

public class CodelinkTest extends DomainTest {

	@Test
	public void linksCanBeFluentlyConstructed() {
		
		Code code = someTarget();
		CodelistLink listLink = someCodelistLink();
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		Codelink link  = like(link().instanceOf(listLink).target(code).attributes(a).build());
		
		assertEquals(listLink,link.type());
		
		System.out.println(reveal(link.type()).state().getClass());
		assertEquals(a,link.attributes().lookup(a.name()));
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Codelink link = modifyLink("1").target(someTarget()).build();
		
		assertEquals(MODIFIED,((Codelink.Private) link).status());

	}
	
	@Test
	public void resolveNames() {
		
		
		Code code = someTarget();
		
		CodelistLink listLink = listLink().name("link").target(someCodelist()).anchorToName().build();
		
		Codelink link  = link().instanceOf(listLink).target(code).build();
		
		assertEquals(code.name(),link.value());
		
	}
	
	@Test
	public void resolveAttribute() {
		
		
		Code code = someTarget();
		
		Attribute a = attribute().name(name).value(value).ofType(DEFAULT_TYPE).in(language).build();
		
		reveal(code).update(reveal(modifyCode(code.id()).attributes(a).build()));
		
		Attribute template = attribute().name(name).ofType(NULL_QNAME).build();
		
		CodelistLink listLink = listLink().name("link").target(someCodelist()).anchorTo(template).build();
		
		Codelink link  = link().instanceOf(listLink).target(code).build();
		
		assertEquals(Arrays.asList(a.value()),link.value());
		
	}
	
	@Test
	public void cloned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		Codelink link = link().instanceOf(someCodelistLink()).target(someTarget()).attributes(a).build();
		
		Codelink.State state = reveal(link).state();
		CodelinkMS clone = new CodelinkMS(state);

		assertEquals(clone,state);
		
		assertFalse(clone.id().equals(state.id()));
		
	}
	
	
	@Test
	public void changeTarget() {
		
		
		Code code = someTarget();
		
		CodelistLink listLink = someCodelistLink();
		
		Codelink link  = link().instanceOf(listLink).target(code).build();
		
		code = someTarget("new");
		
		Codelink changeset = modifyLink(link.id()).target(code).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(code.name(),link.value());
		
	}
	
	private CodelistLink someCodelistLink() {
		
		return like(listLink().name("link").target(someCodelist()).build());
	}
	
	private Codelist someCodelist() {
		
		return like(codelist().name("name").build());
	}

	private Code someTarget() {
		
		return someTarget("name");
	}

	private Code someTarget(String name) {
		
		return like(code().name(name).build());
	}


}
