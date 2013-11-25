package org.acme;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistQueries.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.cdi.ApplicationEvents.ApplicationEvent;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class CodelistRepositoryTest {
	
	@Inject
	CodelistRepository repository;
	
	@Test
	public void retrieveNotExistingCodeList() {

		assertNull(repository.lookup("unknown"));

	}

	@Test
	public void addCodelist() {

		Codelist list = codelist().name("name").build();

		repository.add(list);

		assertEquals(list, repository.lookup(list.id()));

	}

	@Test
	public void updateCodelist() {

		Attribute attribute = attr().name("test").value("val").build();
		Code code = code().name("code").attributes(attribute).build();

		Codelist list = codelist().name("name").with(code).build();

		repository.add(list);

		Attribute attributeChangeset = attr(attribute.id()).name(attribute.name()).value("newvalue").build();

		QName updatedName = q(list.name().getLocalPart() + "-updated");

		Codelist changeset = codelist(list.id()).name(updatedName)
				.with(code(code.id()).name(code.name()).attributes(attributeChangeset).build()).build();

		repository.update(changeset);

		list = repository.lookup(list.id());

		assertEquals(list.name(), updatedName);

		Attribute firstAttribute = list.codes().iterator().next().attributes().iterator().next();

		assertEquals(firstAttribute.value(), "newvalue");
	}

	@Test
	public void removeCodelist() {

		Codelist list = codelist().name("name").build();

		repository.add(list);

		repository.remove(list.id());

		assertNull(repository.lookup(list.id()));

	}
	
	@Test
	public void allCodelists() {
		
		Codelist list = codelist().name("name").build();
		
		repository.add(list);
		
		Iterable<Codelist> lists  = repository.get(allLists());
		
		assertEqualSets(gather(lists),list);
	}
	
	@Test
	public void codeRanges() {
		
		Code code1 = code().name("c1").build();
		Code code2 = code().name("c2").build();
		Code code3 = code().name("c3").build();
		
		Codelist list = codelist().name("l").with(code1,code2,code3).build();
		
		repository.add(list);
		
		System.out.println(list);
		
		Iterable<Code> inrange  = repository.get(allCodesIn(list.id()).from(2).to(3));
		
		assertEquals(asList(code2,code3),inrange);
	}
	
	@Test
	public void listCoordinates() {
		
		Codelist list1 = codelist().name("l1").version("1").build();
		Codelist list2 = codelist().name("l2").version("2").build();
		
		repository.add(list1);
		repository.add(list2);
		
		Iterable<CodelistCoordinates> results  = repository.get(allListCoordinates());
		
		assertEqualSets(gather(results),coords(list1.id(),q("l1"),"1"),coords(list2.id(),q("l2"),"2"));
	}
	
	
	@Test
	public void getSummary() {
		
		Attribute a1 = attr().name("name1").value("v1").ofType("t1").in("l1").build();
		Attribute a2 = attr().name("name2").value("v2").ofType("t2").build();
		Attribute a3 = attr().name("name1").value("v3").ofType("t2").in("l1").build();
		Attribute a4 = attr().name("name1").value("v1").ofType("t1").in("l2").build();
		Attribute a5 = attr().name("name2").value("v2").ofType("t2").build();
		
		Attribute aa1 = attr().name("name1").value("v1").ofType("t3").in("l3").build();
		Attribute aa2 = attr().name("name2").value("v2").ofType("t2").build();
		Attribute aa3 = attr().name("name3").value("v3").ofType("t3").in("l2").build();
		Attribute aa4 = attr().name("name1").value("v4").ofType("t1").in("l3").build();
		
		Code c1 = code().name("c1").attributes(a1,a2,a3).build();
		Code c2 = code().name("name1").attributes(a4,a5).build();
		
		Codelist list = codelist().name("n").with(c1,c2).attributes(aa1,aa2,aa3,aa4).build();
		
		repository.add(list);
		
		CodelistSummary summary = repository.get(summary(list.id()));
		
		assertEquals(q("n"), summary.name());
		assertEquals(2, summary.size());
		
		
		assertEqualSets(summary.allNames(),q("name1"),q("name2"),q("name3"));
		assertEqualSets(summary.allTypes(),q("t1"),q("t2"),q("t3"));
		assertEqualSets(summary.allLanguages(),"l1","l2","l3");
		
		assertEqualSets(summary.allTypesFor(q("name1")),q("t1"),q("t2"),q("t3"));
		assertEqualSets(summary.allLanguagesFor(q("name1"),q("t1")),"l1","l2","l3");
		assertEqualSets(summary.allTypesFor(q("foo")));
		assertEqualSets(summary.allLanguagesFor(q("foo"),q("boo")));
		
		assertEqualSets(summary.codeNames(),q("name1"),q("name2"));
		assertEqualSets(summary.codeTypesFor(q("name1")),q("t1"),q("t2"));
		assertEqualSets(summary.codeLanguagesFor(q("name1"),q("t1")),"l1","l2");
		
	}
	
	@Inject
	Event<ApplicationEvent> events;

	//sadly, we cannot control injection on a per-test basis, so we need to cleanup after each test
	//we do it with end-of-app events
	@After
	public void shutdown() {
		events.fire(Shutdown.INSTANCE);
	}

}
