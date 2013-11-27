package org.cotrix.repository.user;

import javax.enterprise.event.Observes;

import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.domain.user.User;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.user.impl.MUserQueryFactory;
import org.cotrix.repository.user.impl.UserQueryFactory;

public class UserQueries {

	private static UserQueryFactory factory = new MUserQueryFactory();
	
	public static MultiQuery<User,User> allUsers() {
		return factory.allUsers();
	}
	
	public static MultiQuery<User,User> usersWithRoleOn(String resource, ResourceType type) {
		return factory.roleOn(resource, type);
	}
	
	public static Query<User,User> userByName(String name) {
		
		return factory.userByName(name); 
		
	}
	
	
	public static Criterion<User> byName() {
		
		return factory.byName();
	}
	
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, UserQueryFactory factory) {	
			
			UserQueries.factory=factory;
		}
	}
}
