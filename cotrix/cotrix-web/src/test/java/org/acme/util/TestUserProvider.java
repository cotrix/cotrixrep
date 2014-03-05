/**
 * 
 */
package org.acme.util;

import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.inject.Inject;

import org.cotrix.domain.user.User;
import org.cotrix.security.SignupService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestUserProvider {
	
	private static final User testUser = user().name("testUser").email("testUser@fao.org").fullName("Test User").is(ROOT).build();
	private static final String testUserPassword = "testPassword";
	
	@Inject
	private SignupService signupService;
	
	public void createTestUser() {
		signupService.signup(testUser, testUserPassword);
	}

	/**
	 * @return the testuser
	 */
	public User getUser() {
		return testUser;
	}

	/**
	 * @return the testuserpassword
	 */
	public String getPassword() {
		return testUserPassword;
	}
}
