package org.cotrix.security.impl;

import static org.cotrix.common.Utils.*;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.security.Realm;
import org.cotrix.security.tokens.NameAndPassword;

@ApplicationScoped @Native
public abstract class NativeRealm implements Realm {

	@Override
	public boolean supports(Object token) {
		return token instanceof NameAndPassword;
	}
	
	protected abstract String passwordFor(String name);
	
	protected abstract void create(String name, String pwd);
	
	@Override
	public String login(Object token) {
	
		NameAndPassword npwd = reveal(token,NameAndPassword.class);
		
		String pwd  = passwordFor(npwd.name());
		
		if (pwd==null)
			return null;
		
		if (!npwd.password().equals(pwd))
			throw new IllegalStateException("incorrect password for user "+npwd.name());
		
		return npwd.name();
	}
	
	
	@Override
	public void signup(String name, String pwd) {
		
		if (passwordFor(name)!=null)
			throw new IllegalStateException("a user '"+name+"' has already signed up");
		
		create(name,pwd);
	}
	
	
	
}
