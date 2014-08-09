package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.repository.CodelistActions.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class CodelistRepositoryUpdateTest extends ApplicationTest {
	
	@Inject
	CodelistRepository repository;

	@Test
	public void deleteAttributeDef() {
		
		AttributeDefinition def = attrdef().name("name").build();
		Attribute attr1 = attribute().instanceOf(def).value("value").build();
		Attribute attr2 = attribute().instanceOf(def).value("value2").build();
		Code code = code().name("name").attributes(attr1,attr2).build();
		
		Codelist list = codelist().name("name").definitions(def).with(code).build();

		repository.add(list);
		
		repository.update(list.id(),deleteAttrdef(def.id()));
		
		list = repository.lookup(list.id());
		
		code = list.codes().lookup(code.id());
		
		assertFalse(code.attributes().contains(attr1.id()));
		assertFalse(code.attributes().contains(attr2.id()));
		assertFalse(list.definitions().contains(def.id()));
	}
	
	@Test
	public void deleteLinkDefinition() {
		
		Code targetcode1 = code().name("name").build();
		Code targetcode2 = code().name("name").build();
		Codelist target = codelist().name("name").with(targetcode1,targetcode2).build();
		
		repository.add(target);
		
		LinkDefinition link = linkdef().name("name").target(target).build();
		
		Link l1 = link().instanceOf(link).target(targetcode1).build();
		Link l2 = link().instanceOf(link).target(targetcode2).build();
		
		Code code1 = code().name("name").links(l1).build();
		Code code2 = code().name("name").links(l2).build();
		
		Codelist list = codelist().name("name").links(link).with(code1,code2).build();

		repository.add(list);
		
		repository.update(list.id(),deleteLinkdef(link.id()));
		
		list = repository.lookup(list.id());
		
		code1 = list.codes().lookup(code1.id());
		code2 = list.codes().lookup(code2.id());
		
		assertFalse(code1.links().contains(l1.id()));
		assertFalse(code2.links().contains(l2.id()));

		assertFalse(list.links().contains(link.id()));
	}
	
	
	
	
}
