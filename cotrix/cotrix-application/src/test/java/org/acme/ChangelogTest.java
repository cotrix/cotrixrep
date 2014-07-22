package org.acme;

import static java.lang.Thread.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.application.changelog.ChangelogService;
import org.cotrix.application.changelog.Changelog;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Before;
import org.junit.Test;

public class ChangelogTest extends ApplicationTest {

	
	Code c1 = code().name("c").build();
	Codelist list = codelist().name("l").with(c1).build();
	
	@Inject
	ChangelogService changelogs;
	
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
	public void newcode() {
		
		Code newcode = code().name("new").build();
		
		Code noise = delete(list.codes().lookup(c1.qname()));
		
		
		codelists.update(modify(list).with(newcode, noise).build());
		
		Changelog log = changelogs.changelogFor(list);
		
		assertFalse(log.added().isEmpty());
		
		assertEquals(newcode.id(),log.added().get(0).id());
	}
	
	@Test
	public void removedcode() {
		
		Code noise = code().name("new").build();
		
		Code code = list.codes().lookup(c1.qname());
		
		Attribute marker = attribute().with(DELETED).value("any").build(); 
		
		codelists.update(modify(list).with(modify(code).attributes(marker).build(), noise).build());
		
		Changelog log = changelogs.changelogFor(list);
		
		assertFalse(log.deleted().isEmpty());
		
		assertEquals(code.id(),log.deleted().get(0).id());
	}
	
	
	@Test
	public void modifiedcode() throws Exception {
		
		Code noise = code().name("new").build();
		
		Code code = list.codes().lookup(c1.qname());
		
		sleep(1000);
		
		codelists.update(modify(list).with(modify(code).name("nn").build(),noise).build());
		
		Changelog log = changelogs.changelogFor(list);
		
		assertFalse(log.modified().isEmpty());
		
		assertEquals(code.id(),log.modified().get(0).id());
	}
	
	
	
}
