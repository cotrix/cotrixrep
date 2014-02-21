/**
 * 
 */
package org.cotrix.application.impl.mail;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.event.Observes;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.User;
import org.cotrix.security.events.SignupEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public class SignupMailer extends AbstractMailer {

	protected static final String TEMPLATE_NAME = "signup.ftl";
	protected static final String SUBJECT = "[Cotrix] New user registered";

	public void onSignup(@Observes SignupEvent event) {
		
		Iterable<User> roots = usersWithRole(Roles.ROOT);
		
		Collection<String> addresses = addressesOf(roots);
		
		sendMail(addresses, SUBJECT, getText(TEMPLATE_NAME, "user", event.getUser()));
	}

	private Collection<String> addressesOf(Iterable<User> users) {
		Collection<String> names = new ArrayList<String>();
		for (User u : users)
			names.add(u.email());
		return names;
	}

}
