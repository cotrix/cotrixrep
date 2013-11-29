package org.acme;

import static org.cotrix.action.UserAction.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.security.impl.DefaultNameAndPasswordCollector.*;
import static org.junit.Assert.*;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;
import org.cotrix.repository.user.impl.DefaultUserRepository;
import org.cotrix.repository.user.impl.MUserRepository;
import org.cotrix.repository.utils.UuidGenerator;
import org.cotrix.security.impl.DefaultLoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.security.impl.DefaultSignupService;
import org.cotrix.security.impl.MRealm;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@AdditionalClasses({Users.class,DefaultUserRepository.class, MUserRepository.class, DefaultNameAndPasswordCollector.class, MRealm.class, UuidGenerator.class })
public class LoginTest {

	@Inject
	ContextController contextController;
	
	@Inject
	@Current
	User currentUser;

	@Inject
	DefaultLoginService service;
	
	@Inject
	DefaultSignupService signupService;
	
	@Inject
	Event<ApplicationEvents.ApplicationEvent> events;
	
	
	@Test
	public void loginAsGuestIfNoTokenIsAvailable() throws Exception {
	
		DummyHttpRequest req = new DummyHttpRequest();
		
		contextController.openRequest(req);
		
		User logged = service.login(req);

		assertEquals(guest, logged);

		// annoyingly we cannot compare objects as current currentUser is a proxy
		assertEquals(guest.toString(), currentUser.toString());
		
		contextController.closeRequest();
	}

	@Test
	public void signUpAndLogin() throws Exception {
		
		System.out.println(service);
		
		User user = user().name("fabio").email("fabio@me.com").fullName("fifi").build();
		
		signupService.signup(user,"fuffa");
		
		assertTrue(user.can(EDIT.on(user.id())));
		assertEquals(user.email(),"fabio@me.com");
		
		DummyHttpRequest r = new DummyHttpRequest();
		
		contextController.openRequest(r);
		
		r.setAttribute(nameParam, user.name());
		r.setAttribute(pwdParam, "fuffa");
		
		User logged = service.login(r);
		
		assertEquals(logged.name(),user.name());
		
	}
	
	@Produces @SessionScoped
	public @Current BeanSession currentUser() {
	
		return new BeanSession();		
	}
	
	@Produces @RequestScoped
	public @Current User currentUser(@Current BeanSession session) {
	
		return session.get(User.class);		
	}
	
	@After
	public void shutdown() {
		events.fire(Shutdown.INSTANCE);
	}

}
