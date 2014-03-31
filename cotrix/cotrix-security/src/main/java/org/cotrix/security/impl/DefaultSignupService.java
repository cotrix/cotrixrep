package org.cotrix.security.impl;

import static org.cotrix.action.MainAction.*;
import static org.cotrix.action.UserAction.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.SignupService;
import org.cotrix.security.events.SignupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSignupService implements SignupService {

	private static Logger log = LoggerFactory.getLogger(DefaultSignupService.class);
	
	@Inject
	private NativeRealm realm;
	
	@Inject
	private UserRepository repository;
	
	@Inject
	private Event<SignupEvent> events;
	
	@Override
	public void signup(User user, String pwd) {

		notNull("user",user);
		notNull("password",pwd);
		
		realm.add(user.name(),pwd);
		
		//store
		
		repository.add(user);
		
		User changeset = modifyUser(user).is(USER).can(LOGOUT,EDIT.on(user.id()), VIEW.on(user.id()), CHANGE_PASSWORD.on(user.id())).build();
		
		repository.update(changeset);
		
		log.info("{} has signed up", user.name());	
		
		events.fire(new SignupEvent(user));
	}
	
	@Override
	public void changePassword(User user, String oldPwd, String newPwd) {
		
		notNull("user",user);
		notNull("current password",oldPwd);
		notNull("new password",newPwd);
		
		realm.update(user.name(), oldPwd, newPwd);
		
		log.info("user {} has changed password",user.name());	
	}
	
}
