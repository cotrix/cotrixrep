package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.links.OccurrenceRanges.*;
import static org.cotrix.domain.links.ValueFunctions.*;
import static org.cotrix.domain.trait.Status.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkOfLink;
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
		assertEquals(NameLink.INSTANCE,link.valueType());
		assertEquals(identity,reveal(link).state().function());
		assertEquals(arbitrarily,reveal(link).state().range());
		
		Attribute a = attribute().name(name).build();
		
		link  = listLink().name(name).target(list).attributes(a).anchorToName().build();
		
		assertTrue(link.attributes().contains(a));
		
		link  = listLink().name(name).target(list).anchorToName().build();
		
		assertEquals(NameLink.INSTANCE,link.valueType());
		
		Attribute template = attribute().name(q("this")).ofType(q("that")).in("this").build();
		link  = listLink().name(name).target(list).anchorTo(template).build();
		
		assertEquals(new AttributeLink(new AttributeTemplate(template)),link.valueType());
		
		CodelistLink linktemplate = listLink().name(name).target(list).anchorTo(template).build();
		
		link  = listLink().name(name).target(list).anchorTo(linktemplate).build();
		
		assertEquals(new LinkOfLink(linktemplate),link.valueType());
		
		link  = listLink().name(name).target(list).transformWith(lowercase).build();
		
		assertEquals(lowercase,reveal(link).state().function());
		
		link  = listLink().name(name).target(list).occurs(atmost(5)).build();
		
		assertEquals(atmost(5),reveal(link).state().range());
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();
		
		CodelistLink link;
		
		link = modifyListLink("1").name(name).build();
		link =  modifyListLink("1").attributes(a).build();
		link =  modifyListLink("1").anchorTo(a).build();
		link =  modifyListLink("1").anchorToName().build();
		link =  modifyListLink("1").anchorTo(link).build();
		link =  modifyListLink("1").transformWith(uppercase).build();
		link =  modifyListLink("1").occurs(atmostonce).build();
		
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
		
		assertEquals(new AttributeLink(new AttributeTemplate(a)),link.valueType());
		
	}
	
	@Test
	public void changeRange() {
		
		
		CodelistLink link = like(listLink().name(name).target(someTarget()).build());
		
		CodelistLink changeset = modifyListLink(link.id()).occurs(once).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(once,link.range());
		
	}
	
	@Test
	public void changeFunction() {
		
		
		CodelistLink link = like(listLink().name(name).target(someTarget()).build());
		
		CodelistLink changeset = modifyListLink(link.id()).transformWith(custom("'Hello $value'")).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(custom("'Hello $value'"),link.function());
		
	}
	
	private Codelist someTarget() {
		
		return someTarget("name");
	}

	private Codelist someTarget(String name) {
		
		return like(codelist().name(name).build());
	}


}
