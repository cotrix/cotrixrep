package org.acme;

import static org.cotrix.action.UserAction.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.security.impl.DefaultNameAndPasswordCollector.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.security.LoginService;
import org.cotrix.security.SignupService;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class LoginTest extends ApplicationTest {

	@Inject
	@Current
	User currentUser;

	@Inject
	LoginService service;
	
	@Inject
	SignupService signupService;

	HttpServletRequest req = mock(HttpServletRequest.class);

	@Test
	public void loginAsGuestIfNoTokenIsAvailable() throws Exception {
	
		User logged = service.login(req);

		assertEquals(guest, logged);

		//we cannot compare objects as current currentUser is a CDI proxy
		assertEquals(guest.id(), currentUser.id());
		
	}

	@Test
	public void loginAsCotrix() throws Exception {
		
		when(req.getAttribute(nameParam)).thenReturn(cotrix.name());
		when(req.getAttribute(pwdParam)).thenReturn(cotrix.name());
		
		User logged = service.login(req);
		
		assertEquals(logged.name(),cotrix.name());
		
	}
	
	@Test
	public void signUpAndLogin() throws Exception {
		
		User user = user().name("fabio").email("fabio@me.com").fullName("fifi").build();
		
		signupService.signup(user,"any");
		
		assertTrue(user.can(EDIT.on(user.id())));
		assertEquals(user.email(),"fabio@me.com");

		when(req.getAttribute(nameParam)).thenReturn(user.name());
		when(req.getAttribute(pwdParam)).thenReturn("any");

		User logged = service.login(req);
		
		assertEquals(logged.name(),user.name());
		
	}

}
