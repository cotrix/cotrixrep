package org.cotrix.domain.dsl;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.domain.dsl.Roles.*;

import org.cotrix.action.GuestAction;
import org.cotrix.domain.dsl.builder.UserBuilder;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserChangeClause;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserNewClause;
import org.cotrix.domain.user.User;

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
		return new UserBuilder(u.id()).can(u.permissions()).is(u.directRoles());
	}

	// predefined users

	public static User cotrix = new UserBuilder("COTRIX-ID").name("cotrix").noMail().fullName("Cotrix Root User")
			.is(ROOT).build();

	public static User guest = new UserBuilder("GUEST-ID").name("gest").noMail().fullName("Cotrix Guest User")
			.can(GuestAction.values()).can(VIEW).build();

}
