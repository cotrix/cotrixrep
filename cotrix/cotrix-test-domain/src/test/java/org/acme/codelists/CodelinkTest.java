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
		
		assertEquals(a,link.attributes().lookup(a.name()));
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Codelink link = modifyLink("1").target(someTarget()).build();
		
		assertEquals(MODIFIED,((Codelink.Private) link).status());

	}
	
	@Test
	public void resolveLinkToNames() {
		
		
		Code code = like(code().name("a").build());
		Codelist list = like(codelist().name("A").build());  
		
		CodelistLink listLink = like(listLink().name("link").target(list).anchorToName().build());
		
		Codelink link  = like(link().instanceOf(listLink).target(code).build());
		
		assertEquals(code.name(),link.value());
		
	}
	
	@Test
	public void resolveLinkToAttributes() {
		
		
		Attribute a = like(attribute().name(name).value(value).ofType(DEFAULT_TYPE).in(language).build());
		Code code = like(code().name("b").attributes(a).build());
		Codelist list = like(codelist().name("B").with(code).build());  

		Attribute template = attribute().name(name).ofType(NULL_QNAME).build();
		
		CodelistLink listLink = like(listLink().name("link").target(list).anchorTo(template).build());
		Codelink link  = like(link().instanceOf(listLink).target(code).build());
		
		assertEquals(Arrays.asList(a.value()),link.value());
		
	}
	
	@Test
	public void resolveLinkToLinks() {
		
		
		Code code = like(code().name("c").build());
		Codelist list = like(codelist().name("C").build());  
		
		CodelistLink listlink = like(listLink().name("link-to-c").target(list).build());
		
		Codelink link = like(link().instanceOf(listlink).target(code).build());
		code = like(code().name("b").links(link).build());
		list = like(codelist().name("B").links(listlink).with(code).build());
		
		
		listlink = like(listLink().name("link-to-b").target(list).anchorTo(listlink).build());
		link = like(link().instanceOf(listlink).target(code).build());
		
		assertEquals(Arrays.asList(q("c")),link.value());
		
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
