package org.cotrix.user;

import static java.util.Arrays.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;

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

	
	
	
	// pre-defined role models

	// the role of generic users, typically right after sign up: can view and logout
	public static RoleModel USER = user().name("user").fullName("User Role").can(VIEW, LOGOUT).buildAsModel();

	// a user that can edit given codelists.
	public static RoleModel EDITOR = user().name("editor").fullName("Editor Role").is(USER).can(EDIT).buildAsModel();

	//a user that has all the permissions and roles on given codelists. 
	public static RoleModel MANAGER = user().name("manager").fullName("Manager Role").is(USER,EDITOR).can(CodelistAction.values()).buildAsModel();

	//a user that has all the permissions and roles
	public static RoleModel ROOT = user().name("root").fullName("Root Role").can(MainAction.values())
			.can(CodelistAction.values()).can(UserAction.values()).is(USER, MANAGER, EDITOR).buildAsModel();

	public static List<RoleModel> predefinedRoles = asList(ROOT, USER, MANAGER, EDITOR);

	// predefined users

	public static User cotrix = user().name("cotrix").fullName("Cotrix Root User").is(ROOT).build();

	public static User guest = user().name("cotrix-guest").fullName("Cotrix Guest User").can(GuestAction.values())
			.build();

	public static List<User> predefinedUsers = asList(cotrix, guest);

}
