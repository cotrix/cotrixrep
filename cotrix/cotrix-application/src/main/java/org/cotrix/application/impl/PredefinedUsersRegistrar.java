package org.cotrix.application.impl;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.security.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredefinedUsersRegistrar {

	private static Logger log = LoggerFactory.getLogger(PredefinedUsersRegistrar.class);

	public static void loadPredefinedUsers(@Observes Startup event, CodelistRepository repository, SignupService service) {
		
		log.info("signing up predefined users ");
		
		for (User user : Users.predefinedUsers)
			if (user.id()==null) //add them only if they have not been added before
				service.signup(user,user.name());
	}
}
