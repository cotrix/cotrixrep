package org.cotrix.user.dsl;

import java.util.UUID;

public class Users {

	public static UserBuilder user(String name) {
		return user(UUID.randomUUID().toString(), name);
	}
	
	public static UserBuilder user(String id, String name) {
		return new UserBuilder(id, name);
	}
	
}
