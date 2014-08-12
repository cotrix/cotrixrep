package org.acme;

import static org.cotrix.domain.dsl.Entities.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.domain.version.Version;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Before;
import org.junit.Test;

public class VersioningServiceTest extends ApplicationTest {

	@Inject
	VersioningService service;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	TestUser currentUser;
	
	@Inject
	UserRepository users;
	
	@Inject
	Event<Version> events;
	
	Codelist list = codelist().name("test").build();
	
	@Before
	public void before() {
		
		codelists.add(list);
		
		User jimmy = user().name("jimmy").fullName("Jimmy The Manager").noMail().is(OWNER.on(list.id())).build();
		
		currentUser.set(jimmy);
		
		users.add(jimmy);
		
		
	}
	
	@Test
	public void codelistCanBeVersioned() {
		
		String version = list.version();
		
		assertFalse(version.equals("2014"));
		
		Codelist versioned = service.bump(list).to("2014");
		
		assertEquals("2014", versioned.version());
		
	}
	
	@Test
	public void versioningPropagatesPermissions() {
		
		Codelist versioned = service.bump(list).to("2014");
		
		assertEquals("2014", versioned.version());
		
		User persisted = users.lookup(currentUser.id());
		
		assertTrue(persisted.is(OWNER.on(versioned.id())));
	}
}
