package org.acme.codelists;

import static java.util.concurrent.TimeUnit.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Test;

public class CodeManagedTest extends ApplicationTest {
	
	@Inject
	VersioningService versioning;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	TestUser current;
	
	@Inject
	UserRepository users;

	Attribute attr = attribute().name("name").build();
	
	Code code = code()
					.name("name")
					.attributes(attr)
					.build();
	
	@Test
	public void accessCreationDate() {
		
		assertNotNull(code.created());
	}
	
	@Test
	public void accessLastUpdateDate() throws Exception {
		
		
		//default value is creation date
		assertEquals(code.created(),code.lastUpdated());
		
		//let date change
		SECONDS.sleep(1);
		
		reveal(code).update(reveal(modify(code).name("newname").build()));
		
		assertNotEquals(code.created(),code.lastUpdated());
	}
	
	@Test
	public void accessLastUpdateDateBy() throws Exception {
		
		assertNull(code.lastUpdatedBy());
		
		reveal(code).update(reveal(modify(code).name("newname").build()));
		
		//default current user
		assertEquals(current.name(),code.lastUpdatedBy());
		
		User fifi = user().name("fifi").fullName("fifi").email("fifi@invente.com").build();
		users.add(fifi);
		current.set(fifi);
		
		reveal(code).update(reveal(modify(code).name("oncemore").build()));
		
		assertEquals(fifi.name(),code.lastUpdatedBy());		
	}
	
	@Test
	public void accessOrigin() throws Exception {
		
		User fifi = user().name("fifi").fullName("fifi").email("fifi@invente.com").build();
		users.add(fifi);
		current.set(fifi);
		
		//default value is creation date
		assertNull(code.originId());
		assertNull(code.originName());
		
		Codelist list  = codelist().name("newname").with(code).build();
		
		codelists.add(list);
		
		Codelist versioned = versioning.bump(list).to("2.0");
		
		Code vcode = versioned.codes().getFirst(code);
		
		assertEquals(code.id(), vcode.originId());
		assertEquals(code.qname(), vcode.originName());

	}
	
	@Test
	public void accessMarkers() throws Exception {

		Attribute a = attribute().instanceOf(DELETED).value("any").build(); 
		reveal(code).update(reveal(modify(code).attributes(a).build()));
		
		//default value is creation date
		assertFalse(code.markers().isEmpty());
		
		assertNotNull(code.attributes().getFirst(DELETED));
		
		assertEquals(DELETED.qname(),code.markers().get(0).definition().qname());
		
	}
	
}
