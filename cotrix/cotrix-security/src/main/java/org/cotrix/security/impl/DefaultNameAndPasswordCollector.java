package org.cotrix.security.impl;

import org.cotrix.security.LoginRequest;
import org.cotrix.security.TokenCollector;
import org.cotrix.security.tokens.NameAndPassword;

public class DefaultNameAndPasswordCollector implements TokenCollector {

	public static final String nameParam="name";
	public static final String pwdParam="pwd";
	
	@Override
	public Object token(LoginRequest request) {
		
		String name = request.getAttribute(nameParam);
		String pwd = request.getAttribute(pwdParam);
		
		return name!=null && pwd!=null? new NameAndPassword(name,pwd):null;
	}
}
