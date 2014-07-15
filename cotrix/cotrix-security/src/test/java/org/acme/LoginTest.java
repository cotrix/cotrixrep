package org.acme;

import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.security.impl.DefaultNameAndPasswordCollector.*;
import static org.cotrix.test.TestConstants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.InvalidUsernameException;
import org.cotrix.security.LoginRequest;
import org.cotrix.security.LoginService;
import org.cotrix.security.SignupService;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class LoginTest extends ApplicationTest {

	@Inject
	@Current
	BeanSession session;

	@Inject
	LoginService service;

	@Inject
	UserRepository repository;

	@Inject
	SignupService signupService;

	LoginRequest req = mock(LoginRequest.class);

	@Test
	public void loginAsGuestIfNoTokenIsAvailable() throws Exception {

		User logged = service.login(req);

		assertEquals(guest, logged);

		//note: if we'd injected the current user we would not find it changed
		assertEquals(guest, session.get(User.class));

	}

	@Test
	public void usersThatSignUpArePersistedAndLoggedIn() throws Exception {

		String pwd = "pwd";
				
		signupService.signup(joe,pwd);
		
		assertNotNull(repository.lookup(joe.id()));

		when(req.getAttribute(nameParam)).thenReturn(joe.name());
		when(req.getAttribute(pwdParam)).thenReturn(pwd);

		User currentUser = service.login(req);

		assertEquals(joe.id(), currentUser.id());

	}

	@Test
	public void usersCanChangePassword() throws Exception {

		signupService.signup(joe, "old");

		signupService.changePassword(joe, "old", "new");

		when(req.getAttribute(nameParam)).thenReturn(joe.name());
		when(req.getAttribute(pwdParam)).thenReturn("new");

		User currentUser = service.login(req);

		assertEquals(joe.id(), currentUser.id());

	}

	@Test(expected = InvalidUsernameException.class)
	public void identitiesAreUnique() throws Exception {

		signupService.signup(joe, "any");

		User homonymous = user().name(joe.name()).fullName("Joe the Secondo").email("joe@clone.com").build();

		signupService.signup(homonymous, "any");
	}
}
