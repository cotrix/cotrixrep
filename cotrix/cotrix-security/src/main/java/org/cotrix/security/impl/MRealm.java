package org.cotrix.security.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.cotrix.security.Realm;
import org.cotrix.security.Token;
import org.cotrix.security.tokens.NameAndPassword;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MRealm implements Realm<NameAndPassword> {

	private static Logger log = LoggerFactory.getLogger(MRealm.class);
	
	private Map<String,String> pwds = new HashMap<String,String>();
	
	
	@PostConstruct
	public void loadPredefinedUsers() {
	
		log.info("loading predefined users");
		
		for (User user : PredefinedUsers.values)
			pwds.put(user.id(),user.id());
	}
	
	@Override
	public boolean supports(Token token) {
		return token instanceof NameAndPassword;
	}
	
	@Override
	public String login(NameAndPassword token) {
		
		String password = pwds.get(token.name());
		
		
		return password!=null && password.equals(token.password())? token.name():null;
	}
}
