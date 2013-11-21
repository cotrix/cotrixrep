package org.cotrix.security.impl;

import static org.cotrix.common.Utils.*;

import javax.inject.Inject;

import org.cotrix.security.Realm;
import org.cotrix.security.SignupService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSignupService implements SignupService {

	private static Logger log = LoggerFactory.getLogger(DefaultSignupService.class);
	
	@Inject @Native
	private Realm<?> realm;
	
	@Inject
	private UserRepository repository;
	
	@Override
	public void signup(User user, String pwd) {

		notNull("user",user);
		notNull("password",pwd);
		
		realm.signup(user.name(),pwd);
		
		repository.add(user);
		
		log.info("signed up "+user.name());	
	}
	
	
}
