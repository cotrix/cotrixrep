package org.cotrix.security.impl;

import static org.cotrix.action.MainAction.*;
import static org.cotrix.action.UserAction.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.cdi.ApplicationEvents.FirstTime;
import org.cotrix.common.cdi.ApplicationEvents.Ready;
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
		
		User changeset = modifyUser(user).is(USER).can(LOGOUT,EDIT.on(user.id())).build();
		
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
	
	//on first use of a store, register the proto-root user, 'cotrix', with the default password.
	//installation managers will want to change that on first login.
	public static void clear(@Observes @FirstTime Ready event, NativeRealm realm) {
		
		log.info("signing up proto-root {}",cotrix.name());	
		
		realm.add(cotrix.name(),DEFAULT_COTRIX_PWD);
		
	}
	
}
