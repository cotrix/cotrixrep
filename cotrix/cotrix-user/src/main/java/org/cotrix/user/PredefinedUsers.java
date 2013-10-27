package org.cotrix.user;

import static org.cotrix.action.Action.*;
import static org.cotrix.user.dsl.Users.*;

import java.util.Arrays;
import java.util.List;

public class PredefinedUsers {

	//the root user
	public static User cotrix = user("cotrix").fullName("Cotrix Root User").can(allActions).build();
	
	//the guest user TODO: restrict this 
	public static User guest = user("guest").fullName("Cotrix Guest User").can(allActions).build();
	
	public static List<User> values = Arrays.asList(cotrix,guest);
	
}
