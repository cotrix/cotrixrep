package org.cotrix.test;

import static org.cotrix.domain.dsl.Roles.*;

import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;

public class TestConstants {

	public static User joe = Users.user().name("joe").fullName("Joe the Plumber").noMail().is(USER).build();
}
