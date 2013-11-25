package org.cotrix.user.queries;

import javax.enterprise.event.Observes;

import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.repository.Filter;
import org.cotrix.repository.Query;
import org.cotrix.repository.Specification;
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
	
	public static Specification<User,User> userByName(String name) {
		
		return factory.userByName(name); 
		
	}
	
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, UserQueryFactory factory) {	
			
			UserQueries.factory=factory;
		}
	}
}
