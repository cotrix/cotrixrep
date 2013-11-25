package org.cotrix.repository;

import javax.enterprise.event.Observes;

import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.domain.User;
import org.cotrix.repository.impl.UserQueryFactory;
import org.cotrix.repository.memory.MUserQueryFactory;

public class UserQueries {

	private static UserQueryFactory factory = new MUserQueryFactory();
	
	public static MultiQuery<User,User> allUsers() {
		return factory.allUsers();
	}
	
	public static Filter<User> roleOn(String resource, ResourceType type) {
		return factory.roleOn(resource, type);
	}
	
	public static Query<User,User> userByName(String name) {
		
		return factory.userByName(name); 
		
	}
	
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, UserQueryFactory factory) {	
			
			UserQueries.factory=factory;
		}
	}
}
