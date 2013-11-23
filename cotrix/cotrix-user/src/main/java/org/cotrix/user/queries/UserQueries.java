package org.cotrix.user.queries;

import javax.enterprise.event.Observes;

import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.repository.query.Filter;
import org.cotrix.repository.query.Query;
import org.cotrix.user.User;
import org.cotrix.user.impl.UserQueryFactory;
import org.cotrix.user.memory.MUserQueryFactory;

public class UserQueries {

	private static UserQueryFactory factory = new MUserQueryFactory();
	
	public static Query<User,User> allUsers() {
		return factory.allUsers();
	}
	
	public static Filter<User> roleOn(String resource, ResourceType type) {
		return factory.roleOn(resource, type);
	}
	
	//TODO...others
	
	static class CdiInjector {
		
		static void configure(@Observes Startup event, UserQueryFactory factory) {
		
			UserQueries.factory = factory;
		}
		
	}
}
