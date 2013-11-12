package org.cotrix.user;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;

import java.util.Arrays;
import java.util.List;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.GuestAction;
import org.cotrix.action.MainAction;
import org.cotrix.action.UserAction;
import org.cotrix.user.dsl.UserBuilder;
import org.cotrix.user.dsl.UserGrammar.UserChangeClause;
import org.cotrix.user.dsl.UserGrammar.UserNewClause;

/**
 * Predefined users and factory methods.
 * 
 * @author Fabio Simeoni
 *
 */
public class Users {

	
	
	public static UserNewClause user() {
		return new UserBuilder();
	}

	public static UserChangeClause user(String id) {
		return new UserBuilder(id);
	}

	public static UserChangeClause user(User u) {
		return new UserBuilder(u.id()).can(u.permissions()).is(u.roles());
	}

	
	
	//pre-defined role models

	
	public static RoleModel ROOT = user().name("root").fullName("Root Role").can(MainAction.values())
			.can(CodelistAction.values()).can(UserAction.values()).buildAsModel();

	public static RoleModel USER = user().name("user").fullName("New User Role").can(VIEW)
			.buildAsModel();

	public static RoleModel MANAGER = user().name("manager").fullName("Manager Role").can(IMPORT,PUBLISH).buildAsModel();
	
	public static RoleModel EDITOR = user().name("editor").fullName("Editor Role").is(USER).can(EDIT).buildAsModel();

	
	
	//predefined users
	
	public static User cotrix = user().name("cotrix").fullName("Cotrix Root User").is(ROOT).build();

	public static User guest = user().name("cotrix-guest").fullName("Cotrix Guest User").can(GuestAction.values()).build();


	public static List<User> predefinedUsers = Arrays.asList(cotrix, guest);
}
