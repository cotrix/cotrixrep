package org.cotrix.user;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;

import java.util.Arrays;
import java.util.List;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.user.dsl.UserBuilder;
import org.cotrix.user.dsl.UserGrammar.UserChangeClause;
import org.cotrix.user.dsl.UserGrammar.UserNewClause;

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

	
	public static RoleModel ROOT = user().name("ROOT-model").fullName("Root Role Model").can(MainAction.values())
			.can(CodelistAction.values()).buildAsModel();

	public static RoleModel USER = user().name("user-model").fullName("New User Role Model").can(VIEW)
			.buildAsModel();

	public static RoleModel MANAGER = user().name("MANAGER-model").fullName("Manager Role Model").can(IMPORT)
			.can(CodelistAction.values()).buildAsModel();
	
	public static RoleModel EDITOR = user().name("EDITOR-model").fullName("Editor Role Model").is(USER).can(EDIT).buildAsModel();

	
	
	//predefined users
	
	public static User cotrix = user().name("cotrix").fullName("Cotrix Root User").is(ROOT).cannot(LOGIN).build();

	public static User guest = user().name("cotrix-guest").fullName("Cotrix Guest User").can(VIEW, LOGIN).build();


	public static List<User> predefinedUsers = Arrays.asList(cotrix, guest);
}
