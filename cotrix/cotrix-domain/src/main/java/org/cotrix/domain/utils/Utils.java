package org.cotrix.domain.utils;

import static java.text.DateFormat.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.Calendar;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.dsl.Users;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;

public class Utils {

	private static User currentUser;
	private static RequestContext requestContext;
	
	public static Attribute.State stateof(Attribute a) {
		return reveal(a).state();
	}
	
	public static Attribute.State stateof(AttributeGrammar.ValueClause clause) {
		return stateof(clause.build());
	}
	
	public static String time() {
		return getDateTimeInstance().format(Calendar.getInstance().getTime());
	}
	
	public static String currentUser() {
		return requestContext.isActive()?currentUser.name():Users.cotrix.name();
	}
	
	
	static void setUser(@Observes Startup startup,@Current User user, @Current RequestContext context) {
		
		currentUser = user;
		requestContext = context;
	}
}
