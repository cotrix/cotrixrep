package org.cotrix.user.dsl;

import org.cotrix.user.dsl.UserGrammar.UserChangeClause;
import org.cotrix.user.dsl.UserGrammar.UserNewClause;


public class Users {

	public static UserNewClause user() {
		return new UserBuilder();	
	}
	
	public static UserChangeClause user(String id) {
		return new UserBuilder(id);	
	}
	
}
