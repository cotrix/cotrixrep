package org.acme;

import static org.cotrix.test.TestConstants.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Test;

//naming ensures these do not execute @ each build. 
//they prove on demand the mechanisms work.
public class ProducerTests extends ApplicationTest {

	@Inject
	@Current
	User currentUser;

	@Inject
	TestUser testUser;
	
	@Inject
	@Current
	BeanSession currentSession;

	@Test
	public void testUserWrapsCurrentUser() {

		assertEquals(currentUser.id(), testUser.get().id());
	}

	@Test
	public void currentUserIsJoe() {

		assertEquals(joe.id(), currentUser.id());
	}
	
	@Test
	public void testUserIsInSession() {

		assertEquals(testUser.id(), currentSession.get(User.class).id());
	}
}
