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
import org.junit.Test;

public class VersioningServiceTest extends ApplicationTest {

	@Inject
	VersioningService service;
	
	@Inject
	CodelistRepository repository;
	
	
	@Inject
	UserRepository users;
	
	
	@Inject
	CurrentUser currentUser;
	
	@Inject
	Event<Version> events;
	
	@Test
	public void versionCodelist() {
		
		Codelist codelist = codelist().name("test").build();
		
		String version = codelist.version();
		
		assertFalse(version.equals("2014"));
		
		Codelist versioned = service.bump(codelist).to("2014");
		
		assertEquals("2014", versioned.version());
		
	}
	
	@Test
	public void versioningPropagatesPermissions() {
		
		
		Codelist codelist = codelist().name("test").build();
		
		User fifi = user().name("fifi").fullName("fifi").email("dude@me.com").is(OWNER.on(codelist.id())).build();
		
		currentUser.set(fifi);
		
		users.add(fifi);
		
		Codelist versioned = service.bump(codelist).to("2014");
		
		assertEquals("2014", versioned.version());
		
		fifi = users.lookup(fifi.id());
		
		assertTrue(fifi.is(OWNER.on(versioned.id())));
	}
	
	@Test
	public void fetchesVersionsAndPersistsUseCase() {
		
		Codelist codelist = codelist().name("test").version("2013").build();
		
		repository.add(codelist);
		
		Codelist fetched = repository.lookup(codelist.id());
		
		Codelist versioned = service.bump(fetched).to("2014");
		
		repository.add(versioned);
		
		assertNotNull(repository.lookup(versioned.id()));
		
		repository.remove(versioned.id());
		repository.remove(codelist.id());
		
	}
}
