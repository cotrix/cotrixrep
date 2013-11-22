package org.cotrix.user.queries;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.repository.query.Query;
import org.cotrix.user.User;
import org.cotrix.user.impl.UserQueryFactory;
import org.cotrix.user.memory.MUserQueryFactory;

public class UserQueries {

	private static UserQueryFactory factory = new MUserQueryFactory();
	
	public static Query<User,User> allUsers() {
		return factory.allUsers();
	}
	
	
	static class CdiInjector {
		
		static void configure(@Observes ApplicationEvents.Startup event, UserQueryFactory factory) {
		
			UserQueries.factory = factory;
		}
		
	}
}
