package org.cotrix.test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;

public class CdiProducers {

	//simnulates session for services that expect it
	@Produces @Current @ApplicationScoped
	public static BeanSession session(CurrentUser current) {
		
		BeanSession session = new BeanSession();
		
		session.add(User.class,current);

		return session;
	}
	
	
	//produces current user for services that expect it
	@Produces @Current @ApplicationScoped
	public static User user(@Current BeanSession session) {
		return session.get(User.class);
	}
	
	//produces current user for tests that want to control it. it's cotrix by default
	@Produces @ApplicationScoped
	public static CurrentUser current() {
		CurrentUser user = new CurrentUser();
		user.set(Users.cotrix);
		return user;
	}
}
