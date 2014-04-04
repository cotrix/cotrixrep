package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.utils.AttributeTemplate;
import org.junit.Test;

public class CodelistLinkTest extends DomainTest {

	@Test
	public void linksCanBeFluentlyConstructed() {
		
		Codelist list = someTarget();
		
		CodelistLink link  = listLink().name(name).target(list).build();
		
		assertEquals(name,link.name());
		assertEquals(list,link.target());
		assertEquals(NameLink.INSTANCE,link.type());
		
		Attribute a = attribute().name(name).build();
		
		link  = listLink().name(name).target(list).attributes(a).anchorToName().build();
		
		assertTrue(link.attributes().contains(a));
		
		link  = listLink().name(name).target(list).anchorToName().build();
		
		assertEquals(NameLink.INSTANCE,link.type());
		
		Attribute template = attribute().name(q("this")).ofType(q("that")).in("this").build();
		link  = listLink().name(name).target(list).anchorTo(template).build();
		
		assertEquals(new AttributeLink(new AttributeTemplate(template)),link.type());
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		CodelistLink link;
		
		link = modifyListLink("1").name(name).build();
		link =  modifyListLink("1").attributes(a).build();
		link =  modifyListLink("1").anchorTo(a).build();
		link =  modifyListLink("1").anchorToName().build();
		//link =  modifyListLink("1").anchorTo(link).build();
		
		//changed
		assertEquals(MODIFIED,((CodelistLink.Private) link).status());

	}
	
	@Test
	public void cloned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		CodelistLink link = listLink().name(name).target(someTarget()).anchorToName().attributes(a).build();
		
		CodelistLink.State state = reveal(link).state();
		CodelistLinkMS clone = new CodelistLinkMS(state);

		assertEquals(clone,state);
		
		assertFalse(clone.id().equals(state.id()));
		
	}
	
	@Test
	public void changeName() {
		
		CodelistLink link = like(listLink().name(name).target(someTarget("name")).anchorToName().build());
		
		CodelistLink changeset = modifyListLink(link.id()).name(name2).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(name2,link.name());
		
	}
	
	@Test
	public void changeType() {
		
		
		CodelistLink link = like(listLink().name(name).target(someTarget()).anchorToName().build());
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		CodelistLink changeset = modifyListLink(link.id()).anchorTo(a).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(new AttributeLink(new AttributeTemplate(a)),link.type());
		
	}
	
	private Codelist someTarget() {
		
		return someTarget("name");
	}

	private Codelist someTarget(String name) {
		
		return like(codelist().name(name).build());
	}


}
