package org.cotrix.memory.security;

import static org.cotrix.domain.dsl.Users.*;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.security.Realm;
import org.cotrix.security.Token;
import org.cotrix.security.impl.Native;
import org.cotrix.security.tokens.NameAndPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped @Native
public class MRealm implements Realm<NameAndPassword> {

	private static Logger log = LoggerFactory.getLogger(MRealm.class);
	
	private Map<String,String> pwds = new HashMap<String,String>();
	
	@Override
	public boolean supports(Token token) {
		return token instanceof NameAndPassword;
	}
	
	@Override
	public String login(NameAndPassword token) {
	
	
		String password = pwds.get(token.name());

		return password!=null && password.equals(token.password())? token.name():null;
	}
	
	
	@Override
	public void signup(String name, String pwd) {
		pwds.put(name,pwd);
	}
	
	public void clear(@Observes Startup event) {
		pwds.put(cotrix.name(),cotrix.name());

	}
	
	public void clear(@Observes Shutdown event) {
		log.trace("clearing inner realm");
		pwds.clear();
	}
}
