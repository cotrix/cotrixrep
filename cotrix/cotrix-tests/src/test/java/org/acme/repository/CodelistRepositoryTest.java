package org.acme.repository;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistQueries.*;

import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class CodelistRepositoryTest extends ApplicationTest {
	
	@Inject
	CodelistRepository repository;

	@Test
	public void emptyRepo() {

		assertEquals(0,repository.size());

	}
	
	@Test
	public void retrieveUnknownCodeList() {

		assertNull(repository.lookup("unknown"));

	}

	@Test
	public void addCodelist() {

		int size = repository.size();
		
		Codelist list = codelist().name("name").build();

		repository.add(list);

		assertEquals(size+1,repository.size());
		
		assertEquals(list, repository.lookup(list.id()));

	}

	@Test
	public void removeCode() {
		
		Code code = code().name("code").build();
		
		Codelist list = codelist().name("name").with(code).build();

		repository.add(list);
		
		assertTrue(list.codes().contains(code));
		
		repository.update(modifyCodelist(list.id()).with(deleteCode(code.id())).build());
		
		assertFalse(list.codes().contains(code));
		
	}
	
	@Test
	public void addCode() {
		
		Codelist list = codelist().name("name").build();

		repository.add(list);
		
		list = repository.lookup(list.id());

		Code code = code().name("code").build();
		
		assertFalse(list.codes().contains(code));

		repository.update(modifyCodelist(list.id()).with(code).build());
		
		list = repository.lookup(list.id());
		
		assertTrue(list.codes().contains(code));
		
	}
	
	@Test
	public void updateCode() {

		Code code = code().name("code").build();
		
		Codelist list = codelist().name("name").with(code).build();

		repository.add(list);
		
		list = repository.lookup(list.id());

		repository.update(modifyCodelist(list.id()).with(modifyCode(code.id()).name("name2").build()).build());
		
		list = repository.lookup(list.id());
		
		assertTrue(list.codes().contains(q("name2")));
		
	}
	
	@Test
	public void updateAttribute() {

		Attribute a = attribute().name("n").value("v").build();

		Codelist list = codelist().name("n").attributes(a).build();

		repository.add(list);

		Attribute modified = modifyAttribute(a.id()).value("v2").build();

		Codelist changeset = modifyCodelist(list.id()).attributes(modified).build();

		repository.update(changeset);

		list = repository.lookup(list.id());

		assertEquals(list.attributes().lookup(q("n")).value(), "v2");
	}

	@Test
	public void removeCodelist() {

		Codelist list = codelist().name("name").build();

		repository.add(list);

		int size = repository.size();
		
		repository.remove(list.id());

		assertEquals(size-1,repository.size());
		
		assertNull(repository.lookup(list.id()));

	}
	
	@Test(expected=IllegalStateException.class)
	public void removeUnknownCodelist() {

		Codelist list = codelist().name("name").build();

		repository.remove(list.id());

	}
	
	@Test
	public void allCodelists() {
		
		Codelist list = codelist().name("name").build();
		
		repository.add(list);
		
		Iterable<Codelist> lists  = repository.get(allLists());
		
		assertEqualSets(gather(lists),list);
	}
	
	
	@Test
	public void allCodelistsExcludingSome() {
		
		Codelist list1 = codelist().name("name1").build();
		Codelist list2 = codelist().name("name2").build();
		
		repository.add(list1);
		repository.add(list2);
		
		Iterable<Codelist> lists  = repository.get(allLists().excluding(list1.id()));
		
		assertEqualSets(gather(lists),list2);
	}
	
	@Test
	public void allCodelistForRootLikeUser() {
		
		Codelist list = codelist().name("1").build();
		
		repository.add(list);
		
		Action a = action(codelists,"a");
		Role role = user().name("r").noMail().can(a).buildAsRoleFor(codelists);
		
		User u = user().name("joe").noMail().is(role).build();
		
		
		Iterable<CodelistCoordinates> lists  = repository.get(codelistsFor(u));
		
		assertEqualSets(gather(lists),coordsOf(list));
	}
	
	@Test
	public void allCodelistForUser() {
		
		
		Codelist list = codelist().name("1").build();
		Codelist list2 = codelist().name("2").build();
		
		repository.add(list);
		repository.add(list2);
		
		Action a = action(codelists,"a");
		Role role = user().name("r").noMail().can(a).buildAsRoleFor(codelists);
		User u = user().name("joe").noMail().is(role.on(list2.id())).build();
		
		Iterable<CodelistCoordinates> lists  = repository.get(codelistsFor(u));
		
		assertEqualSets(gather(lists),coordsOf(list2));
	}
	
	@Test
	public void codeRanges() {
		
		Code code1 = code().name("c1").build();
		Code code2 = code().name("c2").build();
		Code code3 = code().name("c3").build();
		
		Codelist list = codelist().name("l").with(code1,code2,code3).build();
		
		repository.add(list);
		
		Iterable<Code> inrange  = repository.get(allCodesIn(list.id()).from(2).to(3));
		
		assertEquals(asList(code2,code3),inrange);
	}
	
	@Test
	public void listCoordinates() {
		
		Codelist list1 = codelist().name("l1").version("1").build();
		Codelist list2 = codelist().name("l2").version("2").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<CodelistCoordinates> results  = repository.get(allListCoordinates().sort(byCoordinateName()));
		
		assertEqualSets(gather(results),coords(list1.id(),q("l1"),"1"),coords(list2.id(),q("l2"),"2"));
	}
	
	@Test
	public void listbyName() {
		
		Codelist list1 = codelist().name("l1").build();
		Codelist list2 = codelist().name("l2").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<Codelist> results  = repository.get(allLists().sort(byCodelistName()));
		
		assertEquals(asList(list1,list2),gather(results));
	}
	
	@Test
	public void listbyNameAndVersion() {
		
		Codelist list1 = codelist().name("l2").version("1").build();
		Codelist list2 = codelist().name("l2").version("3").build();
		
		repository.add(list2);
		repository.add(list1);
		
		Iterable<Codelist> results  = repository.get(allLists().sort(all(byCodelistName(),byVersion())));
		
		assertEquals(asList(list1,list2),gather(results));
	}
	
	@Test
	public void listbyCodeName() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		
		Codelist list = codelist().name("l1").with(c2,c1).build();
		
		repository.add(list);
		
		Iterable<Code> results  = repository.get(allCodesIn(list.id()).sort(byCodeName()));
		
		assertEquals(asList(c1,c2),gather(results));
	}
	
	
	@Test
	public void listbyCodeNameDescending() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		Code c3 = code().name("c3").build();
		
		Codelist list = codelist().name("l1").with(c2,c3,c1).build();
		
		repository.add(list);
		
		Iterable<Code> results  = repository.get(allCodesIn(list.id()).sort(descending(byCodeName())));
		
		assertEquals(asList(c3,c2,c1),gather(results));
	}
	
	@Test
	public void listCodesSortedByAttribute() {
		
		Attribute a1 = attribute().name("a").value("1").build();
		Attribute a2 = attribute().name("a").value("2").build();
		Attribute a3 = attribute().name("a").value("0").in("en").build();
		Attribute a4 = attribute().name("a").value("2").build();
		Attribute a5 = attribute().name("a").value("1").build();
		
		Code c1 = code().name("c1").attributes(a1,a4).build();
		Code c2 = code().name("c2").attributes(a2,a5).build();
		Code c3 = code().name("c3").attributes(a3).build();
		
		Codelist list = codelist().name("l1").with(c2,c1,c3).build();
		
		repository.add(list);
		
		Attribute template = attribute().name("a").value("ignore").build();
		Iterable<Code> results  = repository.get(allCodesIn(list.id()).sort(byAttribute(template,1)));
		
		assertEquals(asList(c3,c1,c2),gather(results));
		
		results  = repository.get(allCodesIn(list.id()).sort(byAttribute(template,2)));
		assertEquals(asList(c2,c1,c3),gather(results));
		
		template = attribute().name("a").value("ignore").in("en").build();
		results  = repository.get(allCodesIn(list.id()).sort(byAttribute(template,1)));
		
		assertEquals(asList(c3,c2,c1),gather(results));
	}
	
	
	@Test
	public void getSummary() {
		
		Attribute a1 = attribute().name("name1").value("v1").ofType("t1").in("l1").build();
		Attribute a2 = attribute().name("name2").value("v2").ofType("t2").build();
		Attribute a3 = attribute().name("name1").value("v3").ofType("t2").in("l1").build();
		Attribute a4 = attribute().name("name1").value("v1").ofType("t1").in("l2").build();
		Attribute a5 = attribute().name("name2").value("v2").ofType("t2").build();
		
		Attribute aa1 = attribute().name("name1").value("v1").ofType("t3").in("l3").build();
		Attribute aa2 = attribute().name("name2").value("v2").ofType("t2").build();
		Attribute aa3 = attribute().name("name3").value("v3").ofType("t3").in("l2").build();
		Attribute aa4 = attribute().name("name1").value("v4").ofType("t1").in("l3").build();
		
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
	
}
