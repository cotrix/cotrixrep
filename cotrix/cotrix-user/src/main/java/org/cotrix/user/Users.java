package org.cotrix.user;

import static java.util.Arrays.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.user.Roles.*;

import java.util.List;

import org.cotrix.action.GuestAction;
import org.cotrix.user.dsl.UserBuilder;
import org.cotrix.user.dsl.UserGrammar.UserChangeClause;
import org.cotrix.user.dsl.UserGrammar.UserNewClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predefined users and factory methods.
 * 
 * @author Fabio Simeoni
 * 
 */
public class Users {

	private static Logger log = LoggerFactory.getLogger(Users.class);

	public static UserNewClause user() {
		return new UserBuilder();
	}

	public static UserChangeClause user(String id) {
		return new UserBuilder(id);
	}

	public static UserChangeClause user(User u) {
		return new UserBuilder(u.id()).can(u.permissions()).is(u.roles());
	}

	// predefined users

	public static User cotrix = user().name("cotrix").fullName("Cotrix Root User").is(ROOT).build();

	public static User guest = user().name("guest").fullName("Cotrix Guest User").can(GuestAction.values()).can(VIEW)
			.build();

	public static User cotrix() {
		return user().name("cotrix").fullName("Cotrix Root User").is(ROOT).build();
	}

	public static User guest() {
		return user().name("guest").fullName("Cotrix Guest User").can(GuestAction.values()).can(VIEW).build();
	}

	public static List<User> predefinedUsers = asList(cotrix, guest);
	
	public static List<User> predefinedUsers() {
		return asList(cotrix(), guest());
	}

}
