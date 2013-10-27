package org.cotrix.user;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.dsl.Users.*;

import java.util.Arrays;
import java.util.List;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;

public class PredefinedUsers {

	// the root user
	public static User cotrix = user("cotrix").fullName("Cotrix Root User").can(MainAction.values())
			.can(CodelistAction.values()).cannot(LOGIN).build();

	// the guest user
	public static User guest = user("guest").fullName("Cotrix Guest User").can(LOGIN,VIEW).build();

	public static List<User> values = Arrays.asList(cotrix, guest);
}
