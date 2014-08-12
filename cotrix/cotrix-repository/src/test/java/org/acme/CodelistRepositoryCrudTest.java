package org.acme;

import static org.cotrix.domain.dsl.Data.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
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
	public void retrieveCodelistWithLinks() {

		//prepare a codelist to link to
		Code t1 = code().name("target1").build();
		Code t2 = code().name("target2").build();
		
		Codelist target = addAndRetrieve(codelist().name("linked").with(t1,t2).build());

		//prepare a list that links to target
		LinkDefinition linkdef = linkdef().name("link").target(target).build();
		
		Codelist source = addAndRetrieve(codelist().name("linking").links(linkdef).build());
		
		
		//add two codes with links
		
		t1 = target.codes().getFirst(t1);
		t2 = target.codes().getFirst(t2);
	
		linkdef = source.linkDefinitions().getFirst(linkdef);
		
		Link link1 = link().instanceOf(linkdef).target(t1).build();
		Link link2 = link().instanceOf(linkdef).target(t2).build();
				
		
		Code s1 = code().name("target1").links(link1).build();
		Code s2 = code().name("target2").links(link2).build();

		Codelist changeset = modifyCodelist(source.id()).with(s1,s2).build();
		
		repository.update(changeset);
		
		for (Code code : repository.lookup(source.id()).codes())
			for (Link link : code.links())
				System.out.println(link.definition().qname()+":"+link.value());
		
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
	public void addListLink() {

		Codelist target = addAndRetrieve(codelist().name("name").build());

		LinkDefinition linkdef = linkdef().name("name").target(target).build();
		
		Codelist list = addAndRetrieve(codelist().name("name").build());

		Codelist changeset =  modifyCodelist(list.id()).links(linkdef).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertEquals(NameLink.INSTANCE,retrieved.linkDefinitions().getFirst(linkdef).valueType());
		
	}
	
	@Test
	public void updateCode() {

		Code code = code().name("name").build();
		
		Codelist list = codelist().name("name").with(code).build();

		repository.add(list);
		
		Code modified = modifyCode(code.id()).name("name2").build();
		
		repository.update(modifyCodelist(list.id()).with(modified).build());
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.codes().contains(modified));
		
	}
	
	@Test
	public void updateLinkName() {

		Codelist target = addAndRetrieve(codelist().name("name").build());

		LinkDefinition link = linkdef().name("name").target(target).build();
		
		Codelist list = addAndRetrieve(codelist().name("name").links(link).build());

		LinkDefinition targetChangeset =  modifyLinkDef(link.id()).name("name2").build();
		
		Codelist changeset =  modifyCodelist(list.id()).links(targetChangeset).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.linkDefinitions().contains(targetChangeset));
		
	}
	
	@Test
	public void updateLinkTarget() {

		Codelist target = addAndRetrieve(codelist().name("name1").build());

		LinkDefinition linkdef = linkdef().name("name").target(target).build();
		
		Codelist list = addAndRetrieve(codelist().name("name").links(linkdef).build());

		Attribute a = attribute().name("n").value("v").build();
		
		LinkDefinition modlinkdef =  modifyLinkDef(linkdef.id()).anchorTo(a).build();
		
		Codelist changeset =  modifyCodelist(list.id()).links(modlinkdef).build();
		
		repository.update(changeset);
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertTrue(retrieved.linkDefinitions().getFirst(linkdef).valueType() instanceof AttributeLink);
		
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

		assertEquals(retrieved.attributes().getFirst(a).value(), "v2");
	}
	
	@Test
	public void removeCodelist() {

		AttributeDefinition def = attrdef().name("name").build();
		Attribute a = attribute().instanceOf(def).value("val").build();
		Code c = code().name("name").attributes(a).build();
		Codelist list = codelist().name("name").definitions(def).with(c).build();

		repository.add(list);

		int size = repository.size();
		
		repository.remove(list.id());

		assertEquals(size-1,repository.size());
		
		Codelist retrieved = repository.lookup(list.id());
		
		assertNull(retrieved);

	}
	
	@Test(expected=CodelistRepository.UnremovableCodelistException.class)
	public void codelistCannotBeRemovedIfOthersLinkToIt() {

		
		Codelist list = addAndRetrieve(codelist().name("name").build());

		//establish a link
		LinkDefinition link = linkdef().name("name").target(list).build();
		
		addAndRetrieve(codelist().name("name").links(link).build());

		repository.remove(list.id());

	}
	
	@Test
	public void removeLinkingCodelistDoesNotRemoveLinkedCodelist() {

		
		Codelist list = addAndRetrieve(codelist().name("name").build());

		//establish a link
		LinkDefinition link = linkdef().name("name").target(list).build();
		
		Codelist ref = addAndRetrieve(codelist().name("name").links(link).build());

		repository.remove(ref.id());

		assertNotNull(repository.lookup(list.id()));

		
	}
	
	@Test(expected=IllegalStateException.class)
	public void removeUnknownCodelist() {

		Codelist list = codelist().name("name").build();

		repository.remove(list.id());

	}
	
	//helpers
	
	private Codelist addAndRetrieve(Codelist list) {
		
		repository.add(list);
		return repository.lookup(list.id());
	
	}
}
