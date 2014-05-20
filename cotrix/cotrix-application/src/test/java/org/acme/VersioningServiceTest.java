package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
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
import org.cotrix.test.CurrentUser;
import org.junit.Before;
import org.junit.Test;

public class VersioningServiceTest extends ApplicationTest {

	@Inject
	VersioningService service;
	
	@Inject
	CodelistRepository codelists;
	
	
	@Inject
	UserRepository users;
	
	
	@Inject
	CurrentUser currentUser;
	
	@Inject
	Event<Version> events;
	
	Codelist codelist = codelist().name("test").build();
	
	User fifi = user().name("fifi").fullName("fifi").email("dude@me.com").is(OWNER.on(codelist.id())).build();
	
	
	@Before
	public void before() {
		
		currentUser.set(fifi);
		
		users.add(fifi);
		
		codelists.add(codelist);
		
	}
	
	@Test
	public void codelistCanBeVersioned() {
		
		String version = codelist.version();
		
		assertFalse(version.equals("2014"));
		
		Codelist versioned = service.bump(codelist).to("2014");
		
		assertEquals("2014", versioned.version());
		
	}
	
	@Test
	public void versioningPropagatesPermissions() {
		
		Codelist versioned = service.bump(codelist).to("2014");
		
		assertEquals("2014", versioned.version());
		
		fifi = users.lookup(fifi.id());
		
		assertTrue(fifi.is(OWNER.on(versioned.id())));
	}
}
