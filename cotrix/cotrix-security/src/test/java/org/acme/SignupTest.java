package org.acme;

import static org.cotrix.user.Users.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.security.SignupService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class SignupTest {

	@Inject
	SignupService signupService;

	@Inject
	UserRepository repository;

	@Test
	public void signUp() throws Exception {

		User user = user().name("fabio").fullName("fifi").build();
		
		signupService.signup(user,"fuffa");
		
		assertNotNull(repository.lookupByName("fabio"));
		
	}

}
