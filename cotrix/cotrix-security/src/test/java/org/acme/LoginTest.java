package org.acme;

import static org.cotrix.security.impl.DefaultNameAndPasswordCollector.*;
import static org.cotrix.user.Users.*;
import static org.junit.Assert.*;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.repository.utils.UuidGenerator;
import org.cotrix.security.impl.DefaultLoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.security.impl.MRealm;
import org.cotrix.user.User;
import org.cotrix.user.impl.MUserRepository;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@AdditionalClasses({MUserRepository.class, DefaultNameAndPasswordCollector.class, MRealm.class, UuidGenerator.class })
public class LoginTest {

	@Inject
	ContextController contextController;
	
	@Inject
	@Current
	User currentUser;

	@Inject
	DefaultLoginService service;
	
	//initialises the repository to add default users
	@Inject @New
	MUserRepository repository;

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
	public void loginByNameAndPasswords() throws Exception {

		DummyHttpRequest r = new DummyHttpRequest();
		
		contextController.openRequest(r);
		
		r.setAttribute(nameParam, cotrix.id());
		r.setAttribute(pwdParam, cotrix.id());

		User logged = service.login(r);
		
		assertEquals(cotrix,logged);
		
		// annoyingly we cannot compare objects as current user is a proxy
		assertEquals(cotrix.toString(), currentUser.toString());
		
		contextController.closeRequest();
		
	}

}
