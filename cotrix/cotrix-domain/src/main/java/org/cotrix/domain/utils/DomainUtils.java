package org.cotrix.domain.utils;

import static java.text.DateFormat.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.Calendar;

import javax.enterprise.event.Observes;

import org.cotrix.common.events.ApplicationLifecycleEvents.Startup;
import org.cotrix.common.events.Current;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.user.User;
import org.jboss.weld.context.RequestContext;

public class DomainUtils {

	//sometimes there is no user yet (startup) and we cannot distribute under inactive scopes
	//nor can we tell Weld proxies to fallback to a default if scopes are inactive.
	//sp, we mask under @Current a check on the active scopes and let clients pull the user from us.
	//they can tell us what's a reasonable fallback, otherwise we choose 'cotrix' that stands for the app.
	//this gives them total control: may decide to act as if cotrix was a user, or they may avoid doing if
	//the get back the very fallback they provided.
	

	private static User currentUser;
	private static RequestContext requestContext;

	static void setUser(@Observes Startup startup,@Current User user, @Current RequestContext context) {	
		currentUser = user;
		requestContext = context;
	}
		
	public static User currentUser() {
		return currentUserOr(cotrix);
	}
	
	public static User currentUserOr(User fallback) {
		return requestContext.isActive()?currentUser:fallback;
	}
	
	
	/////////////////////////////////////
	
	public static Attribute.State stateof(Attribute a) {
		return reveal(a).state();
	}
	
	public static Attribute.State stateof(AttributeGrammar.ValueClause clause) {
		return stateof(clause.build());
	}
	
	public static String time() {
		return getDateTimeInstance().format(Calendar.getInstance().getTime());
	}
	
	public static String signatureOf(Codelist list) {
		return String.format("%s  (%s v.%s)",list.id(),list.qname(),list.version());
	}
	
	
}