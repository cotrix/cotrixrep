package org.cotrix.user;

import static org.cotrix.action.Actions.*;
import static org.cotrix.user.dsl.Users.*;

import java.util.Arrays;
import java.util.List;

public class PredefinedUsers {

	//the root user
	public static User cotrix = user("cotrix").can(action(any)).build();
	
	//the guest user TODO: restrict this 
	public static User guest = user("guest").can(action(any)).build();
	
	public static List<User> values = Arrays.asList(cotrix,guest);
	
}
