package org.cotrix.test;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.cotrix.common.Constants;
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;
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
	
	//produces current user for tests that want to control it. it's cotrix by default
	@Produces @ApplicationScoped @Alternative
	public static CurrentUser current() {
		CurrentUser user = new CurrentUser();
		user.set(Users.cotrix);
		return user;
	}
}
