package org.cotrix.user.dsl;


public class Users {

	public static UserBuilder user(String name) {
		return new UserBuilder(name);	
	}
	
}
