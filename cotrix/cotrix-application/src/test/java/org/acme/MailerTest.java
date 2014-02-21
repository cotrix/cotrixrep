package org.acme;

import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.MailService;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.SignupService;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

@Priority(TEST)
public class MailerTest extends ApplicationTest {

	@Inject
	MailService service;
	
	@Inject
	SignupService signupService;
	
	@Inject
	UserRepository repository;
	
	@Produces @Alternative @Singleton
	static MailService mockIt() {
		return mock(MailService.class);
	}
	
	@Test
	public void mailOnSignup() {
		
		User user = user().name("fifi").email("fifi@me.com").fullName("fifi").build();
		
		//ad at least one root
		repository.add(Roles.role("some").isRoot().build());
		
		signupService.signup(user,"any");
			
		verify(service).sendMessage(anyCollectionOf(String.class), anyString(),anyString());
	}
	
}
