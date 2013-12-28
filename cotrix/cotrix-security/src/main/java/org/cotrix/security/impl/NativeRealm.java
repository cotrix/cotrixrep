package org.cotrix.security.impl;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.security.Realm;
import org.cotrix.security.Token;
import org.cotrix.security.tokens.NameAndPassword;

@ApplicationScoped @Native
public abstract class NativeRealm implements Realm<NameAndPassword> {

	@Override
	public boolean supports(Token token) {
		return token instanceof NameAndPassword;
	}
	
	protected abstract String passwordFor(String name);
	
	protected abstract void create(String name, String pwd);
	
	@Override
	public String login(NameAndPassword token) {
	
		String pwd  = passwordFor(token.name());
		
		if (pwd==null)
			return null;
		
		if (!token.password().equals(pwd))
			throw new IllegalStateException("incorrect password for user "+token.name());
		
		return token.name();
	}
	
	
	@Override
	public void signup(String name, String pwd) {
		
		if (passwordFor(name)!=null)
			throw new IllegalStateException("a user '"+name+"' has already signed up");
		
		create(name,pwd);
	}
	
	
	
}
