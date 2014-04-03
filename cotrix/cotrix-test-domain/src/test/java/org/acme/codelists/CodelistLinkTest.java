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
	public void linkCanBeFluentlyConstructed() {
		
		Codelist list = someTarget();
		
		CodelistLink link  = listLink().name(name).target(list).onName().build();
		
		assertEquals(name,link.name());
		
		assertEquals(list,link.target());
		
		Attribute a = attribute().name(name).build();
		
		link  = listLink().name(name).target(list).attributes(a).onName().build();
		
		assertTrue(link.attributes().contains(a));
		
		link  = listLink().name(name).target(list).onName().build();
		
		assertEquals(NameLink.INSTANCE,link.type());
		
		Attribute template = attribute().name(q("this")).ofType(q("that")).in("this").build();
		link  = listLink().name(name).target(list).onAttribute(template).build();
		
		assertEquals(new AttributeLink(new AttributeTemplate(template)),link.type());
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		Codelist list = someTarget();
		
		CodelistLink link;
		
		link = modifyListLink("1").name(name).build();
		link =  modifyListLink("1").target(list).build();
		link =  modifyListLink("1").attributes(a).build();
		link =  modifyListLink("1").target(list).attributes(a).build();
		link =  modifyListLink("1").target(list).attributes(a).build();
		
		//changed
		assertEquals(MODIFIED,((CodelistLink.Private) link).status());

	}
	
	@Test
	public void cloned() {
		
		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		CodelistLink link = listLink().name(name).target(someTarget()).onName().attributes(a).build();
		
		CodelistLink.State state = reveal(link).state();
		CodelistLinkMS clone = new CodelistLinkMS(state);

		assertEquals(clone,state);
		
		assertFalse(clone.id().equals(state.id()));
		
	}
	
	@Test
	public void changeName() {
		
		CodelistLink link = like(listLink().name(name).target(someTarget("name")).onName().build());
		
		CodelistLink changeset = modifyListLink(link.id()).name(name2).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(name2,link.name());
		
	}
	
	@Test
	public void changeTarget() {
		
		
		CodelistLink link = like(listLink().name(name).target(someTarget("name")).onName().build());
		
		Codelist anotherTarget = someTarget("name2");
		
		CodelistLink changeset = modifyListLink(link.id()).target(anotherTarget).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(anotherTarget,link.target());
		
	}
	
	private Codelist someTarget() {
		
		return someTarget("name");
	}

	private Codelist someTarget(String name) {
		
		Codelist list = like(codelist().name(name).build());
		reveal(list).state().status(PERSISTED);
		return list;
	}


}
