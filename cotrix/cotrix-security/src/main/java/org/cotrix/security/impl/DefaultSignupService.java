package org.cotrix.security.impl;

import static org.cotrix.action.UserAction.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.Realm;
import org.cotrix.security.SignupService;
import org.cotrix.security.events.SignupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSignupService implements SignupService {

	private static Logger log = LoggerFactory.getLogger(DefaultSignupService.class);
	
	@Inject @Native
	private Realm<?> realm;
	
	@Inject
	private UserRepository repository;
	
	@Inject
	private Event<SignupEvent> events;
	
	@Override
	public void signup(User user, String pwd) {

		notNull("user",user);
		notNull("password",pwd);
		
		realm.signup(user.name(),pwd);
		
		repository.add(user);
		
		User changeset = modifyUser(user).can(EDIT.on(user.id())).build();
		
		repository.update(changeset);
		
		log.info("signed up "+user.name());	
		
		events.fire(new SignupEvent(user));
	}
	
	
}
