package org.cotrix.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.cotrix.common.BeanSession;
import org.cotrix.common.Constants;
import org.cotrix.common.events.Current;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.context.unbound.Unbound;

@Priority(Constants.TEST)
public class CdiProducers {

	//simnulates session for services that expect it
	@Produces @Current @ApplicationScoped @Alternative
	public static BeanSession session(CurrentUser current) {
		
		BeanSession session = new BeanSession();
		
		session.add(User.class,current);

		return session;
	}
	
	//produces current user for services that expect it
	@Produces @Current @ApplicationScoped @Alternative
	public static User user(@Current BeanSession session) {
		return session.get(User.class);
	}
	
	@Produces @Current @Alternative
	static RequestContext context(@Unbound RequestContext ctx) {
		ctx.activate();
		return ctx;
	}
	
	@Produces @ApplicationScoped @Current @Alternative
	static Map<String,Object> sessionStorage() {
		return new HashMap<>();
	}
	
	@Produces @ApplicationScoped @Current @Alternative
	static BoundSessionContext sessionContext(@Current Map<String,Object> storage, BoundSessionContext ctx) {
		ctx.associate(storage);
		ctx.activate();
		return ctx;
	}
	
	//produces current user for tests that want to control it. it's cotrix by default
	@Produces @ApplicationScoped @Alternative
	public static CurrentUser current() {
		CurrentUser user = new CurrentUser();
		user.set(Users.cotrix);
		return user;
	}
}
