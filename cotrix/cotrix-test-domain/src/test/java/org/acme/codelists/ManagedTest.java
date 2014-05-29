package org.acme.codelists;

import static java.util.concurrent.TimeUnit.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.domain.utils.ManagedCode.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.domain.utils.ManagedCode;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.CurrentUser;
import org.junit.Before;
import org.junit.Test;

public class ManagedTest extends ApplicationTest {
	
	@Inject
	VersioningService versioning;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	CurrentUser current;
	
	@Inject
	UserRepository users;

	Attribute attr = attribute().name(name).build();
	
	Code code = code()
					.name(name)
					.attributes(attr)
					.build();
		
	ManagedCode managed;
	
	
	
	@Before
	public void setup() {
		
		managed = manage(code);
	
	}
	
	@Test
	public void accessCreationDate() {
		
		assertNotNull(managed.created());
	}
	
	@Test
	public void accessLastUpdateDate() throws Exception {
		
		
		//default value is creation date
		assertEquals(managed.created(),managed.lastUpdated());
		
		//let date change
		SECONDS.sleep(1);
		
		reveal(code).update(reveal(modify(code).name(newname).build()));
		
		assertNotEquals(managed.created(),managed.lastUpdated());
	}
	
	@Test
	public void accessOrigin() throws Exception {
		
		User fifi = user().name("fifi").fullName("fifi").email("fifi@invente.com").build();
		users.add(fifi);
		current.set(fifi);
		
		//default value is creation date
		assertNull(managed.originId());
		assertNull(managed.originName());
		
		Codelist list  = codelist().name(name).with(code).build();
		
		codelists.add(list);
		
		Codelist versioned = versioning.bump(list).to(newversion);
		
		ManagedCode managedVersioned = manage(versioned.codes().lookup(managed.name()));
		
		assertEquals(managed.id(), managedVersioned.originId());
		assertEquals(managed.name(), managedVersioned.originName());

	}
	
}
