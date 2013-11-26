/**
 * 
 */
package org.cotrix.web.share.server.util;

import static org.cotrix.domain.dsl.Users.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.User;
import org.cotrix.security.SignupService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserLoader {
	
	@Inject
	protected SignupService signupService;
	
	public void loadUsers() {
		
		
		User user = user().name("federico").fullName("Federico De Faveri").is(Roles.USER, Roles.EDITOR).build();
		signupService.signup(user, "federico");
		
		user = user().name("fabio").fullName("Fabio Simeoni").is(Roles.USER, Roles.EDITOR, Roles.REVIEWER).build();
		signupService.signup(user, "fabio");
		
		user = user().name("anton").fullName("Anton Ellenbroek").is(Roles.USER, Roles.EDITOR, Roles.MANAGER, Roles.PUBLISHER).build();
		signupService.signup(user, "anton");
		
		user = user().name("aureliano").fullName("Aureliano Gentile").is(Roles.ROOT).build();
		signupService.signup(user, "aureliano");
		
		user = user().name("albert").fullName("Albert Einstein").is(Roles.USER).build();
		signupService.signup(user, "albert");
		
	}

}
