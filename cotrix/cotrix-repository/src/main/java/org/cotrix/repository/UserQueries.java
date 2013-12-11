package org.cotrix.repository;

import javax.enterprise.event.Observes;

import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.impl.UserQueryFactory;

public class UserQueries {

	private static UserQueryFactory factory;
	
	public static MultiQuery<User,User> allUsers() {
		return factory.allUsers();
	}
	
	public static MultiQuery<User,User> usersWithRoleOn(String resource, ResourceType type) {
		return factory.roleOn(resource, type);
	}
	
	public static MultiQuery<User,User> usersWithRole(Role role) {
		return factory.role(role);
	}
	
	public static MultiQuery<User,User> teamFor(String codelistId) {
		return factory.teamFor(codelistId);
	}
	
	public static Query<User,User> userByName(String name) {
		
		return factory.userByName(name); 
		
	}
	
	public static <T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2) {
		
		return factory.all(c1,c2);
	}

	public static <T> Criterion<T> descending(Criterion<T> c) {
		
		return factory.descending(c);
	}

	public static Criterion<User> byName() {
		
		return factory.byName();
	}
	
	
	public static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, UserQueryFactory factory) {	
			
			UserQueries.factory=factory;
		}
	}
}
