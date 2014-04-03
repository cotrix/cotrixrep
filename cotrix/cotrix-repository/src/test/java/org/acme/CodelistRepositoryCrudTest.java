package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.links.NameLink;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class CodelistRepositoryCrudTest extends ApplicationTest {
	
	@Inject
	private CodelistRepository repository;

		
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
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertEquals(list,retrieved);

	}
	

	@Test
	public void removeCode() {
		
		Code code = code().name("name").build();
		
		Codelist list = codelist().name("name").with(code).build();
			
		repository.add(list);
		
		assertTrue(list.codes().contains(code));
		
		repository.update(modifyCodelist(list.id()).with(deleteCode(code.id())).build());
		
		Codelist retrieved = repository.lookup(list.id());
	
		assertFalse(retrieved.codes().contains(code));
		
	}
	
	@Test
	public void addCode() {
		
		Codelist list = codelist().name("name").build();

		repository.add(list);
		
		list = repository.lookup(list.id());

		Code code = code().name("code").build();
		
		assertFalse(list.codes().contains(code));

		repository.update(modifyCodelist(list.id()).with(code).build());
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.codes().contains(code));
		
	}
	
	@Test
	public void addLink() {

		Codelist list = codelist().name("name").build();

		repository.add(list);

		Codelist target = codelist().name("name").build();

		repository.add(target);
		
		CodelistLink link = listLink().name("name").target(target).build();
		
		Codelist changeset =  modifyCodelist(list.id()).links(link).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertEquals(NameLink.INSTANCE,retrieved.links().lookup(q("name")).type());
		
	}
	
	
	
	@Test(expected=IllegalArgumentException.class)
	public void cannotAddCodelistWithInvalidLink() {

		Codelist target = codelist().name("name").build();
		CodelistLink link = listLink().name("name").target(target).build();
		
		Codelist list = codelist().name("name").links(link).build();
	
		repository.add(list);
	
	}
	
	@Test
	public void updateCode() {

		Code code = code().name("name").build();
		
		Codelist list = codelist().name("name").with(code).build();

		repository.add(list);
		
		repository.update(modifyCodelist(list.id()).with(modifyCode(code.id()).name("name2").build()).build());
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.codes().contains(q("name2")));
		
	}
	
	@Test
	public void updateLinkName() {

		Codelist target = codelist().name("name").build();

		repository.add(target);
		
		
		CodelistLink link = listLink().name("name").target(target).build();
		
		Codelist list = codelist().name("name").links(link).build();

		repository.add(list);
		
		CodelistLink targetChangeset =  modifyListLink(link.id()).name("name2").build();
		
		Codelist changeset =  modifyCodelist(list.id()).links(targetChangeset).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.links().contains(q("name2")));
		
	}
	
	@Test
	public void updateLinkTarget() {

		Codelist target1 = codelist().name("name1").build();

		repository.add(target1);
		
		Codelist target2 = codelist().name("name2").build();

		repository.add(target2);
		
		CodelistLink link = listLink().name("name").target(target1).build();
		
		Codelist list = codelist().name("name").links(link).build();

		repository.add(list);
		
		CodelistLink targetChangeset =  modifyListLink(link.id()).target(target2).build();
		
		Codelist changeset =  modifyCodelist(list.id()).links(targetChangeset).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertEquals(target2,retrieved.links().lookup(q("name")).target());
		
	}
	
	@Test
	public void updateAttribute() {

		Attribute a = attribute().name("n").value("v").build();

		Codelist list = codelist().name("n").attributes(a).build();

		repository.add(list);

		Attribute modified = modifyAttribute(a.id()).value("v2").build();

		Codelist changeset = modifyCodelist(list.id()).attributes(modified).build();

		repository.update(changeset);

		Codelist retrieved = repository.lookup(list.id());

		assertEquals(retrieved.attributes().lookup(q("n")).value(), "v2");
	}
	
	@Test
	public void removeLink() {

		Codelist target = codelist().name("name").build();

		repository.add(target);
		
		CodelistLink link = listLink().name("name").target(target).build();
		
		Codelist list = codelist().name("name").links(link).build();

		repository.add(list);
		
		CodelistLink targetChangeset =  deleteListLink(link.id());
		
		Codelist changeset =  modifyCodelist(list.id()).links(targetChangeset).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertFalse(retrieved.links().contains(q("name2")));
		
	}

	@Test
	public void removeCodelist() {

		Codelist list = codelist().name("name").build();

		repository.add(list);

		int size = repository.size();
		
		repository.remove(list.id());

		assertEquals(size-1,repository.size());
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertNull(retrieved);

	}
	
	@Test(expected=CodelistRepository.UnremovableCodelistException.class)
	public void codelistCannotBeRemovedIfOthersLinkToIt() {

		
		Codelist list = codelist().name("name").build();

		repository.add(list);
		
		
		//establish a link
		CodelistLink link = listLink().name("name").target(list).build();
		
		Codelist ref = codelist().name("name").links(link).build();

		repository.add(ref);
		
		repository.remove(list.id());

	}
	
	@Test
	public void removeLinkingCodelistDoesNotRemoveLinkedCodelist() {

		
		Codelist list = codelist().name("name").build();

		repository.add(list);
		
		
		//establish a link
		CodelistLink link = listLink().name("name").target(list).build();
		
		Codelist ref = codelist().name("name").links(link).build();

		repository.add(ref);
		
		repository.remove(ref.id());
		
		
		assertNotNull(repository.lookup(list.id()));

		
	}
	
	@Test(expected=IllegalStateException.class)
	public void removeUnknownCodelist() {

		Codelist list = codelist().name("name").build();

		repository.remove(list.id());

	}
	
}
