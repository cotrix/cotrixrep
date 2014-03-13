package org.cotrix.security.impl;

import static org.cotrix.common.Utils.*;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.security.InvalidCredentialsException;
import org.cotrix.security.InvalidUsernameException;
import org.cotrix.security.Realm;
import org.cotrix.security.tokens.NameAndPassword;
import org.jasypt.util.password.BasicPasswordEncryptor;

@ApplicationScoped
public abstract class NativeRealm implements Realm {

	BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
	
	@Override
	public boolean supports(Object token) {
		return token instanceof NameAndPassword;
	}
	
	protected abstract String passwordFor(String name);
	
	protected abstract void create(String name, String pwd);
	
	protected abstract void update(String name, String pwd);
	
	public void update(String name,String oldPwd,String newPwd) {
		
		String current = passwordFor(name);
		
		if (current==null || !encryptor.checkPassword(oldPwd,current))
			throw new InvalidCredentialsException();
			
		update(name,encrypt(newPwd));
	}
	
	@Override
	public String login(Object token) {
	
		NameAndPassword npwd = reveal(token,NameAndPassword.class);
		
		String pwd  = passwordFor(npwd.name());
		
		if (pwd==null)
			return null;
		
		if (encryptor.checkPassword(npwd.password(), pwd))
			 return npwd.name();
					 
		throw new InvalidCredentialsException();
	}
	
	@Override
	public void add(String name, String pwd) {
		
		if (passwordFor(name)!=null)
			throw new InvalidUsernameException(name);
		
		create(name,encrypt(pwd));
	}
	
	//helper
	String encrypt(String plain) {
		
		return encryptor.encryptPassword(plain);
	}
	
}
