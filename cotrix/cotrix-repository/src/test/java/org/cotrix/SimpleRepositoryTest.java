package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Change.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodebagRepository;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MCodebagRepository;
import org.cotrix.repository.memory.MCodelistRepository;
import org.cotrix.repository.memory.MStore;
import org.junit.Test;

public class SimpleRepositoryTest {
	

	@Test
	public void retrieveNotExistingCodeList() {
		
		CodelistRepository repository = new MCodelistRepository();
		
		assertNull(repository.lookup("unknown"));
		
	}
	
	@Test
	public void addCodelist() {
		
		Codelist list = anylist();
		
		assertNull(list.id());
		
		CodelistRepository repository = new MCodelistRepository();
		
		repository.add(list);
		
		assertNotNull(list.id());
		
		assertEquals(list,repository.lookup(list.id()));
		
		
	}
	
	@Test
	public void updateCodelist() {
		
		Attribute attribute = attr().name("test").value("val").build();
		Code code = code().name("code").attributes(attribute).build();
		Codelist list = codelist().name("name").with(code).build();
		
		assertNull(list.id());
		
		CodelistRepository repository = new MCodelistRepository();
		
		repository.add(list);
		
		Attribute attributeChangeset = attr(attribute.id()).name(attribute.name()).value("newvalue").as(MODIFIED).build();
		
		QName updatedName = q(list.name().getLocalPart()+"-updated");
		
		Codelist changeset = codelist(list.id()).name(updatedName).with(code(code.id()).name(code.name()).attributes(attributeChangeset).build()).build();
				
		repository.update(changeset);
		
		list = repository.lookup(list.id());
		
		assertEquals(list.name(),updatedName);
		
		Attribute firstAttribute = list.codes().iterator().next().attributes().iterator().next();
		
		assertEquals(firstAttribute.value(),"newvalue");
	}
	
	
	@Test
	public void removeCodelist() {
		
		Codelist list = anylist();
		
		assertNull(list.id());
		
		CodelistRepository repository = new MCodelistRepository();
		
		repository.add(list);
		
		assertNotNull(list.id());
		
		assertEquals(list,repository.lookup(list.id()));
		
		repository.remove(list.id());
		
		assertNull(repository.lookup(list.id()));
		
	}
	
	
	@Test
	public void addCodebag() {
		
		Codelist list = anylist();
		Codebag bag = anybagWith(list);
		
		assertNull(list.id());
		assertNull(bag.id());
		
		MStore store = new MStore();
		
		CodebagRepository repository = new MCodebagRepository(store);
		
		repository.add(bag);
		
		assertNotNull(bag.id());
		assertNotNull(bag.lists().iterator().next().id());
		
		assertEquals(bag,repository.lookup(bag.id()));
		
		CodelistRepository listRepository = new MCodelistRepository(store);
		
		assertNotNull(listRepository.lookup(list.id()));
		
	}
	
	@Test
	public void removeCodebag() {
		
		Codelist list = anylist();
		Codebag bag = anybagWith(list);
		
		assertNull(list.id());
		assertNull(bag.id());
		
		MStore store = new MStore();
		
		CodebagRepository repository = new MCodebagRepository(store);
		
		repository.add(bag);
		
		assertNotNull(bag.id());
		assertNotNull(list.id());
		
		assertEquals(bag,repository.lookup(bag.id()));
		
		CodelistRepository listRepository = new MCodelistRepository(store);
		
		assertNotNull(listRepository.lookup(list.id()));
		
		repository.remove(bag.id());
		
		assertNull(repository.lookup(bag.id()));
		
		assertEquals(list,listRepository.lookup(list.id()));
	}
	
	
	
	
	//helper
	Codelist anylist() {
		return anylist(null);
	}
	
	//helper
	Codelist anylist(String id) {
		return (id==null?codelist():codelist(id)).name("name").build();
	}
	
	//helper
	Codebag anybagWith(Codelist ...codelists) {
		return codebag().name("name").with(codelists).build();
	}
}
