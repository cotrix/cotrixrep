package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.repository.CodelistActions.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class CodelistRepositoryUpdateTest extends ApplicationTest {
	
	@Inject
	CodelistRepository repository;

	@Test
	public void deleteAttributeDefinition() {
		
		Definition def = definition().name("name").build();
		Attribute attr1 = attribute().with(def).value("value").build();
		Attribute attr2 = attribute().with(def).value("value2").build();
		Code code = code().name("name").attributes(attr1,attr2).build();
		
		Codelist list = codelist().name("name").definitions(def).with(code).build();

		repository.add(list);
		
		repository.update(list.id(),deleteDefinition(def.id()));
		
		list = repository.lookup(list.id());
		
		code = list.codes().lookup(code.id());
		
		assertFalse(code.attributes().contains(attr1.id()));
		assertFalse(code.attributes().contains(attr2.id()));
		assertFalse(list.definitions().contains(def.id()));
	}
	
	
	
	
}
