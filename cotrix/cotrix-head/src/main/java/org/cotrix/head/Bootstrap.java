package org.cotrix.head;

import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.FirstTime;
import org.cotrix.common.cdi.ApplicationEvents.Ready;
import org.cotrix.security.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
	
	private final static Logger log = LoggerFactory.getLogger(Bootstrap.class);

	//on first use of a store, register the proto-root user, 'cotrix', with the default password.
	//installation managers will want to change that on first login.
	public static void clear(@Observes @FirstTime Ready event, SignupService service) {
		
		log.info("signing up proto-root {}",cotrix.name());	
		
		service.signup(cotrix,DEFAULT_COTRIX_PWD);
		
	}
}
