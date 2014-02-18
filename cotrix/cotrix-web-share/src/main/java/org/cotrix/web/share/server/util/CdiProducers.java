package org.cotrix.web.share.server.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;

public class CdiProducers {
	
	@Produces @SessionScoped
	public @Current BeanSession session() {
	
		return new BeanSession();		
	}
	
	@Produces @RequestScoped
	public @Current User currentUser(@Current BeanSession session) {
	
		try {
			return session.get(User.class);
		}
		catch(IllegalStateException e) {

			//this should never happen: let's be more specific
			throw new IllegalAccessError("detected an attempt to access a protected resource without an authenticated user: is the authentication barrier configured?");
			
		}		
	}
}
