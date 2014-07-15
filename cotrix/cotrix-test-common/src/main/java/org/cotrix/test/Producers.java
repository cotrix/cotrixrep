package org.cotrix.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.cotrix.common.BeanSession;
import org.cotrix.common.Constants;
import org.cotrix.common.async.TaskManagerProvider;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.context.unbound.Unbound;

//these alternatives have the highest priority @ test time
//they should be singletong, but @ApplicationScope lets us inject them in session/request scoped beans

@Priority(Constants.TEST)
public class Producers {

	//pre-loaded with test user
	@Produces @Current @Alternative
	public @ApplicationScoped BeanSession session(TestUser current) {
		
		BeanSession session = new BeanSession();
		
		session.add(User.class,current);

		return session;
	}
	
	//this is extracted from the session above, like in production.
	//it's the test user, unless some production code sets it to something else (e.g. @t login time).
	@Produces @Current @Alternative
	public @ApplicationScoped User user(@Current BeanSession session) {
		return session.get(User.class);
	}
	

	//see DomainUtils for how this is used.
	//in essence: we publish a fake but active RequestContet as the @Current context
	//DomainUtils uses the context to know if there is a current user to avoid producing scoped proxies
	//doomed to fail. At test time we want to distribute our test users, so we pretend there is a context.
	//this is Weld specific.
	@Produces @Current @Alternative
	static @ApplicationScoped RequestContext context(@Unbound RequestContext ctx) {
		ctx.activate();
		return ctx;
	}
	
	
	@Produces @ApplicationScoped @Alternative
	TaskManagerProvider testManager(final BoundSessionContext ctx, final BoundRequestContext rctx, @Current final Map<String, Object> storage ) {
		
		return new TaskManagerProvider() {
			
			@Override
			public TaskManager get() {
				return new TaskManager() {
					
					@Override
					public void started() {
						
						rctx.associate(storage);
						rctx.activate();
						
						ctx.associate(storage);
						ctx.activate();
						
					}
					
					@Override
					public void finished() {
						
						ctx.dissociate(storage);
						rctx.dissociate(storage);
						
					}
				};
			}
		};
	}
	
	

	@Produces @ApplicationScoped @Current
	static Map<String, Object> sessionStorage() {
		return new HashMap<>();
	}
}
