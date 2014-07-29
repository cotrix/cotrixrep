package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Before;
import org.junit.Test;

public class ChangelogTest extends ApplicationTest {

	
	Attribute a = attribute().name("a").build();
	Attribute same = attribute().name("same").build();
	Code origin = code().name("c").attributes(a,same).build();
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
		
		assertNotNull(managed.attribute(MODIFIED).value());
		
	}
	
	@Test
	public void codenameChangesAreDetected() {
		
		Code versionedCode = list.codes().lookup(origin);
		
		Code modified = modify(versionedCode).name("modified").build();
		
		codelists.update(modify(list).with(modified).build());
		
		String val = manage(versionedCode).attribute(MODIFIED).value();
		
		System.out.println(val);
		
	}
	
	@Test
	public void attrChangesAreDetected() throws Exception {
		
		Code code = list.codes().lookup(origin);
		
		Attribute attribute = code.attributes().lookup(a);
		
		Attribute mattr = modify(attribute).name("aa").in("en").ofType(NAME_TYPE).value("someval").build();
		Attribute nattr = attribute().name("b").value("val").build();	
		
		Code modified = modify(code).name("cc").attributes(mattr,nattr).build();
		
		codelists.update(modify(list).with(modified).build());
		
		System.out.println(manage(code).attribute(MODIFIED).value());
	
		Thread.sleep(1000);
		
		User fifi = user().name("fifi").fullName("fifi").noMail().build();
	
		user.set(fifi);
		
		Attribute nattr2 = attribute().name("c").value("val").build();
		
		modified = modify(code).attributes(nattr2).build();
		
		codelists.update(modify(list).with(modified).build());

		System.out.println(manage(code).attribute(MODIFIED).value());

	}
	
}
