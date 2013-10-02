package org.acme;

import static org.cotrix.security.impl.DefaultNameAndPasswordCollector.*;
import static org.cotrix.user.PredefinedUsers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.repository.memory.MUserRepository;
import org.cotrix.security.impl.DefaultLoginService;
import org.cotrix.security.impl.DefaultNameAndPasswordCollector;
import org.cotrix.security.impl.MRealm;
import org.cotrix.user.User;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.DummyHttpRequest;
import org.jglue.cdiunit.InSessionScope;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@InSessionScope
@AdditionalClasses({ DummyHttpRequest.class, MUserRepository.class, DefaultNameAndPasswordCollector.class, MRealm.class })
public class LoginTest {

	@Inject
	@Current
	User currentUser;

	@Inject
	DefaultLoginService service;

	@Test
	public void loginAsGuestIfNoTokenIsAvailable() throws Exception {

		User logged = service.login(new DummyHttpRequest());

		assertEquals(guest, logged);

		// annoyingly we cannot compare objects as current currentUser is a proxy
		assertEquals(guest.toString(), currentUser.toString());
	}

	@Test
	public void loginByNameAndPasswords() throws Exception {

		DummyHttpRequest r = new DummyHttpRequest();
		r.getParameterMap().put(nameParam, new String[] { cotrix.name() });
		r.getParameterMap().put(pwdParam, new String[] { cotrix.name() });

		User logged = service.login(r);
		
		assertEquals(cotrix,logged);
		
		// annoyingly we cannot compare objects as current currentUser is a proxy
		assertEquals(cotrix.toString(), currentUser.toString());
		
	}

}
