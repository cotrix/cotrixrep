package org.cotrix.test;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.cotrix.common.BeanSession;
import org.cotrix.common.Constants;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.Unbound;

//alternatives with highest priority @ test time

@Priority(Constants.TEST)
public class Producers {

	//pre-loaded with test user
	@Produces @Current @Alternative
	public @ApplicationScoped BeanSession session(TestUser current) {
		
		BeanSession session = new BeanSession();
		
		session.add(User.class,current);

		return session;
	}
	
	//extracted from current session (see below)
	@Produces @Current @Alternative
	public @ApplicationScoped User user(@Current BeanSession session) {
		return session.get(User.class);
	}
	

	@Produces @Current @Alternative
	static @ApplicationScoped RequestContext context(@Unbound RequestContext ctx) {
		ctx.activate();
		return ctx;
	}
}
