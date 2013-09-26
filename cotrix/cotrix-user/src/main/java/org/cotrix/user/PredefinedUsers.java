package org.cotrix.user;

import static org.cotrix.action.Actions.*;
import static org.cotrix.user.dsl.Users.*;

public class PredefinedUsers {

	//the root user
	public static User cotrix = user("cotrix").can(action(any)).build();
}
