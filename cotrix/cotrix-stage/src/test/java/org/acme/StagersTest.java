package org.acme;

import static org.cotrix.repository.CodelistQueries.*;
import static org.junit.Assert.*;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.events.ApplicationLifecycleEvents.Startup;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.stage.data.SomeUsers;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Test;

public class StagersTest extends ApplicationTest {

	@Inject
	UserRepository users;
	
	@Inject
	CodelistRepository codelists;
	
	public void setup(@Observes Startup event,TestUser user) {
		
		user.set(SomeUsers.owners.iterator().next());
	}
	
	@Test
	public void stage() {
		
		for (User user : SomeUsers.users)
			assertNotNull(users.lookup(user.id()));
		
		for (Codelist list : codelists.get(allLists())) {
			for (Code code: list.codes())
				for (Link link : code.links())
					System.out.println(link.qname()+":"+link.value());
		}
	}
	
}
