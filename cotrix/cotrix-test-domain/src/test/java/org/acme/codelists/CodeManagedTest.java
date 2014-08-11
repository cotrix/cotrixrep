package org.acme.codelists;

import static java.util.concurrent.TimeUnit.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributes;
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
		
		assertNotNull(code.attributes().dateOf(CREATED));
	}
	
	@Test
	public void accessLastUpdateDate() throws Exception {
		
		Attributes attributes = code.attributes();
		
		//default value is creation date
		assertFalse(attributes.contains(LAST_UPDATED));
		
		//let date change
		SECONDS.sleep(1);
		
		reveal(code).update(reveal(modify(code).name("newname").build()));
		
		assertNotNull(attributes.dateOf(LAST_UPDATED));
	}
	
	@Test
	public void accessLastUpdateDateBy() throws Exception {
		
		Attributes attributes = code.attributes();
		
		assertNull(attributes.dateOf(LAST_UPDATED));
		
		reveal(code).update(reveal(modify(code).name("newname").build()));
		
		//default current user
		assertEquals(current.name(),attributes.valueOf(UPDATED_BY));
		
		User fifi = user().name("fifi").fullName("fifi").email("fifi@invente.com").build();
		users.add(fifi);
		current.set(fifi);
		
		reveal(code).update(reveal(modify(code).name("oncemore").build()));
		
		assertEquals(fifi.name(),attributes.valueOf(UPDATED_BY));		
	}
	
	@Test
	public void accessOrigin() throws Exception {
		
		Attributes attributes = code.attributes();
		
		User fifi = user().name("fifi").fullName("fifi").email("fifi@invente.com").build();
		users.add(fifi);
		current.set(fifi);
		
		//default value is creation date
		assertNull(attributes.valueOf(PREVIOUS_VERSION_ID));
		assertNull(attributes.nameOf(PREVIOUS_VERSION_NAME));
		
		Codelist list  = codelist().name("newname").with(code).build();
		
		codelists.add(list);
		
		Codelist versioned = versioning.bump(list).to("2.0");
		
		Code vcode = versioned.codes().getFirst(code);
		
		attributes = vcode.attributes();
		
		assertEquals(code.id(), attributes.valueOf(PREVIOUS_VERSION_ID));
		assertEquals(code.qname(), attributes.nameOf(PREVIOUS_VERSION_NAME));

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
