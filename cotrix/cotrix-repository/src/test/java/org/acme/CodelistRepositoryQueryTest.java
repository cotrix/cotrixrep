package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistQueries.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class CodelistRepositoryQueryTest extends ApplicationTest {
	
	@Inject
	CodelistRepository repository;

	@Test
	public void allCodelists() {
		
		Codelist list = codelist().name("name").build();
		Codelist list2 = codelist().name("name2").build();
		
		repository.add(list);
		repository.add(list2);
		
		Iterable<Codelist> lists  = repository.get(allLists());
		
		assertEqualUnordered(collect(lists),list,list2);
	}
	
	
	@Test
	public void allCodelistsExcludingSome() {
		
		Codelist list1 = codelist().name("name1").build();
		Codelist list2 = codelist().name("name2").build();
		
		repository.add(list1);
		repository.add(list2);
		
		Iterable<Codelist> lists  = repository.get(allLists().excluding(list1.id()));
		
		assertEqualUnordered(collect(lists),list2);
	}
	
	
	@Test
	public void codesInCodelist() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		Code c3 = code().name("c3").build();
		Codelist list = codelist().name("name").with(c1,c2,c3).build();
		
		repository.add(list);
		
		Iterable<Code> codes  = repository.get(codes(asList(c1.id(),c3.id())));
		
		assertEquals(collect(codes),asList(c1,c3));
	}
	
	@Test
	public void codeLookup() {
		
		Code c1 = code().name("c1").build();
		Codelist list = codelist().name("name").with(c1).build();
		
		repository.add(list);
		
		Code code  = repository.get(code(c1.id()));
		
		assertEquals(code,c1);
	}
	
	@Test
	public void allCodelistForRootLikeUser() {
		
		Codelist list = codelist().name("1").build();
		
		repository.add(list);
		
		Action a = action(codelists,"a");
		Role role = role("r").can(a).buildAsRoleFor(codelists);
		
		User u = user().name("joe").fullName("joe").noMail().is(role).build();
		
		
		Iterable<CodelistCoordinates> lists  = repository.get(codelistsFor(u));
		
		assertEqualUnordered(collect(lists),coordsOf(list));
	}
	
	@Test
	public void allCodelistForUser() {
		
		
		Codelist list = codelist().name("1").build();
		Codelist list2 = codelist().name("2").build();
		
		repository.add(list);
		repository.add(list2);
		
		Action a = action(codelists,"a");
		Role role = role("r").can(a).buildAsRoleFor(codelists);
		User u = user().name("joe").fullName("joe").noMail().is(role.on(list2.id())).build();
		
		Iterable<CodelistCoordinates> lists  = repository.get(codelistsFor(u));
		
		assertEqualUnordered(collect(lists),coordsOf(list2));
	}
	
	@Test
	public void allCodes() {
		
		Code code1 = code().name("c1").build();
		Code code2 = code().name("c2").build();
		Code code3 = code().name("c3").build();
		
		Codelist list = codelist().name("l").with(code1,code2,code3).build();
		
		repository.add(list);
		
		Iterable<Code> codes  = repository.get(allCodesIn(list.id()));
		
		assertEqualOrdered(collect(codes),code1,code2,code3);
	}
	
	@Test
	public void codeRanges() {
		
		Code code1 = code().name("c1").build();
		Code code2 = code().name("c2").build();
		Code code3 = code().name("c3").build();
		
		Codelist list = codelist().name("l").with(code1,code2,code3).build();
		
		repository.add(list);
		
		Iterable<Code> inrange  = repository.get(allCodesIn(list.id()).from(2).to(3));
		
		assertEquals(asList(code2,code3),collect(inrange));
	}
	
	@Test
	public void listCoordinates() {
		
		Codelist list1 = codelist().name("l1").version("1").build();
		Codelist list2 = codelist().name("l2").version("2").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<CodelistCoordinates> results  = repository.get(allListCoordinates().sort(byCoordinateName()));
		
		assertEqualUnordered(collect(results),coords(list1.id(),q("l1"),"1"),coords(list2.id(),q("l2"),"2"));
	}
	
	@Test
	public void listbyName() {
		
		Codelist list1 = codelist().name("l1").build();
		Codelist list2 = codelist().name("l2").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<Codelist> results  = repository.get(allLists().sort(byCodelistName()));
		
		assertEquals(asList(list1,list2),collect(results));
	}
	
	@Test
	public void listbyNameAndVersion() {
		
		Codelist list1 = codelist().name("l2").version("1").build();
		Codelist list2 = codelist().name("l2").version("3").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<Codelist> results  = repository.get(allLists().sort(all(byCodelistName(),byVersion())));
		
		assertEquals(asList(list1,list2),collect(results));
	}
	
	@Test
	public void listbyCodeName() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		
		Codelist list = codelist().name("l1").with(c2,c1).build();
		
		repository.add(list);
		
		Iterable<Code> results  = repository.get(allCodesIn(list.id()).sort(byCodeName()));
		
		assertEquals(asList(c1,c2),collect(results));
	}
	
	
	@Test
	public void listbyCodeNameDescending() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		Code c3 = code().name("c3").build();
		
		Codelist list = codelist().name("l1").with(c2,c3,c1).build();
		
		repository.add(list);
		
		Iterable<Code> results = repository.get(allCodesIn(list.id()).sort(descending(byCodeName())));
		
		assertEquals(asList(c3,c2,c1),collect(results));
	}
	
	@Test
	public void listCodesSortedByAttribute() {
		
		
		Attribute a1 = attribute().name("a").value("1").build();
		Attribute a12 = attribute().name("a").value("5").build();
		Attribute b = attribute().name("b").value("3").build();
		Code c1 = code().name("c1").attributes(a1,a12,b).build();
		
		Attribute a2 = attribute().name("a").value("2").in("en").build();
		Attribute a22 = attribute().name("a").value("3").build();
		Attribute b1 = attribute().name("b").value("3").build();
		Code c2 = code().name("c2").attributes(a2,a22,b1).build();
		
		Attribute a3 = attribute().name("a").value("0").in("en").build();
		Attribute b2 = attribute().name("b").value("4").build();
		Code c3 = code().name("c3").attributes(a3,b2).build();
		
		Codelist list = codelist().name("l1").with(c1,c2,c3).build();
		
		repository.add(list);
		
		Attribute template = attribute().name("a").value("ignore").build();
		
		Iterable<Code> results  = repository.get(allCodesIn(list.id()).sort(byAttribute(template,1)));
		
		assertEquals(asList(c3,c1,c2),collect(results));
		
		results = repository.get(allCodesIn(list.id()).sort(descending(byAttribute(template,1))));
		
		assertEquals(asList(c2,c1,c3),collect(results));
		
		results = repository.get(allCodesIn(list.id()).sort(byAttribute(template,2)));
		assertEquals(asList(c2,c1,c3),collect(results));
		
		template = attribute().name("a").value("ignore").in("en").build();
		
		results  = repository.get(allCodesIn(list.id()).sort(byAttribute(template,1)));
		
		assertEquals(asList(c3,c2,c1),collect(results));
	}
	
	
	@Test
	public void getSummary() {
		
		AttributeDefinition d1 = attrdef().name("name1").is("t1").in("l1").build();
		AttributeDefinition d2 = attrdef().name("name2").is("t2").build();
		AttributeDefinition d3 = attrdef().name("name1").is("t2").in("l1").build();
		AttributeDefinition d4 = attrdef().name("name1").is("t1").in("l2").build();
		AttributeDefinition d5 = attrdef().name("name2").is("t2").build();
		
		Attribute aa1 = attribute().name("name1").value("v1").ofType("t3").in("l3").build();
		Attribute aa2 = attribute().name("name2").value("v2").ofType("t2").build();
		Attribute aa3 = attribute().name("name3").value("v3").ofType("t3").in("l2").build();
		Attribute aa4 = attribute().name("name1").value("v4").ofType("t1").in("l3").build();
		
		Codelist list = codelist().name("n").definitions(d1,d2,d3,d4,d5).attributes(aa1,aa2,aa3,aa4).build();
		
		repository.add(list);
		
		CodelistSummary summary = repository.get(summary(list.id()));
		
		assertEquals(q("n"), summary.name());
		
		assertEqualUnordered(summary.allNames(),q("name1"),q("name2"),q("name3"));
		
		assertEqualUnordered(summary.codelistNames(),q("name1"),q("name2"),q("name3"));
		assertEqualUnordered(summary.codelistTypesFor(q("name1")),q("t1"),q("t3"));
		assertEqualUnordered(summary.codelistLanguagesFor(q("name1"),q("t1")),"l3");
		
		assertEqualUnordered(summary.codeNames(),q("name1"),q("name2"));
		//assertEqualUnordered(summary.codeTypesFor(q("name1")),q("t1"),q("t2"));
		assertEqualUnordered(summary.codeLanguagesFor(q("name1"),q("t1")),"l1","l2");
		
	}
	
}
