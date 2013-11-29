package org.acme;

import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.inject.Produces;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;

public class CdiProducers {

	@Produces @Current
	public static BeanSession session() {
		BeanSession session = new BeanSession();
		session.add(User.class,user());
		return session;
	}
	
	@Produces @Current
	public static User user() {
		return cotrix;
	}
}
