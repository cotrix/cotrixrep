package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest extends ApplicationTest {

	
	AttributeDefinition def = definition().name("def").occurs(once).build();
	Attribute a = attribute().instanceOf(def).value("something").build();
	Code code = code().name("c").attributes(a).build();
	Codelist list = codelist().name("l").with(code).build();
	
	@Inject
	CodelistRepository codelists;
	
//	@Inject
//	UserRepository users;
//	
//	@Inject
//	TestUser user;
	
	@Before
	public void versionList() {
		
		codelists.add(list);
		
	}
	
	@Test
	public void attributeOccurrencesAreValidated() {
		
		Attribute onetoomany = attribute().instanceOf(def).build();
		
		Code modified = modify(code).attributes(onetoomany).build();
		
		codelists.update(modify(list).with(modified).build());
		
		ManagedCode managed = manage(list.codes().lookup(modified.id()));
		
		System.out.println(managed.attribute(INVALID).description());
		
		assertNotNull(managed.attribute(INVALID));
		
		
		modified = modify(code).attributes(delete(onetoomany)).build();
		
		codelists.update(modify(list).with(modified).build());
		
		assertNull(managed.attribute(INVALID));
	}
	
	
	
}
