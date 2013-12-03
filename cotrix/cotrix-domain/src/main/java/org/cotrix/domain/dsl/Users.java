package org.cotrix.domain.dsl;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.domain.dsl.Roles.*;

import org.cotrix.action.GuestAction;
import org.cotrix.common.Utils;
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

	public static User cotrix = user().name("cotrix").noMail().fullName("Cotrix Root User").is(ROOT).build();

	public static User guest = user().name("guest").noMail().fullName("Cotrix Guest User").can(GuestAction.values()).can(VIEW)
			.build();
	
	static {
		reveal(cotrix).setId("COTRIX-ID");
		reveal(guest).setId("GUEST-ID");
	}
	
	private static User.Private reveal(User u) {
		return Utils.reveal(u,User.Private.class);
	}

}
