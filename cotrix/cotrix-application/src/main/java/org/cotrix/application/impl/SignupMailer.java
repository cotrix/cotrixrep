/**
 * 
 */
package org.cotrix.application.impl;

import javax.enterprise.event.Observes;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.security.events.SignupEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SignupMailer extends AbstractMailer {
	
	protected static final String TEMPLATE_NAME = "signup.ftl";
	protected static final String SUBJECT = "[Cotrix] New user registered";
	
	public void onSignup(@Observes SignupEvent event) {
		sendMail(usersWithRole(Roles.ROOT), SUBJECT, getText(TEMPLATE_NAME, "user", event.getUser()));
	}

}
