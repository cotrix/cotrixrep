package org.cotrix.web.common.server.util;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import org.cotrix.common.BeanSession;
import org.cotrix.common.Constants;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.http.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//alternatives below have max priority if deployed in production
@Priority(Constants.RUNTIME) 
public class CdiProducers {

	public static Logger log = LoggerFactory.getLogger(CdiProducers.class);
	
	//the alternative when working at regime is session-scoped (obviously)
	@Produces @Current @Alternative
	public @SessionScoped BeanSession session() {
	
		return new BeanSession();		
	}
	
	//the alternative when working at regime is request-scoped (during login can change)
	@Produces @Current @Alternative
	public @RequestScoped User currentUser(@Current BeanSession session) {
		
		try {
			return session.get(User.class);
		}
		catch(IllegalStateException e) {

			//this should never happen: let's be more specific
			throw new IllegalAccessError("detected an attempt to access a protected resource without an authenticated user: is the authentication barrier configured?");
			
		}	
		
	}
	
	//at regime, the current request context request-scoped
	@Produces @Current @Alternative
	public RequestContext request(@Http RequestContext ctx) {
		return ctx;		
	}

}
