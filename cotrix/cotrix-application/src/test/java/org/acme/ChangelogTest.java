package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Before;
import org.junit.Test;

public class ChangelogTest extends ApplicationTest {

	
	Attribute a = attribute().name("a").build();
	Code origin = code().name("c").attributes(a).build();
	Codelist list = codelist().name("l").with(origin).build();
	
	@Inject
	VersioningService service;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	UserRepository users;
	
	@Inject
	TestUser user;
	
	@Before
	public void versionList() {
		
		users.add(user.get());
		
		codelists.add(list);
		
		list = service.bump(list).to("2");
		
		codelists.add(list);
		
	}
	
	@Test
	public void newCodesAreMarked() {
		
		Code code = code().name("newcode").build();
		
		codelists.update(modify(list).with(code).build());

		ManagedCode managedCode = manage(list.codes().lookup(code.qname()));
		
		assertNotNull(managedCode.attribute(NEW));
		
	}
	
	@Test
	public void modifiedCodesAreMarked() {
		
		Code versionedCode = list.codes().lookup(origin);
		
		Code modified = modify(versionedCode).name("modified").build();
		
		codelists.update(modify(list).with(modified).build());

		ManagedCode managed = manage(list.codes().lookup(modified.qname()));
		
		assertNull(managed.attribute(NEW));
		
		assertNotNull(managed.attribute(MODIFIED));
		
		assertNotNull(managed.attribute(MODIFIED).description());
		
	}
	
	@Test
	public void codenameChangesAreDetected() {
		
		Code versionedCode = list.codes().lookup(origin);
		
		Code modified = modify(versionedCode).name("modified").build();
		
		codelists.update(modify(list).with(modified).build());
		
		System.out.println(manage(versionedCode).attribute(MODIFIED).description());
		
	}
	
	@Test
	public void attrChangesAreDetected() {
		
		Code code = list.codes().lookup(origin);
		
		Attribute attribute = code.attributes().lookup(a);
		
		Attribute mattr = modify(attribute).name("aa").in("en").ofType(NAME_TYPE).value("someval").build();
		Attribute nattr = attribute().name("b").value("val").build();	
		
		Code modified = modify(code).name("cc").attributes(mattr,nattr).build();
		
		codelists.update(modify(list).with(modified).build());
		
		System.out.println(manage(code).attribute(MODIFIED).description());
	
	}
	
}
