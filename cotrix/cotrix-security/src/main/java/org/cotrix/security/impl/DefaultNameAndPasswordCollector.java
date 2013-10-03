package org.cotrix.security.impl;

import javax.servlet.http.HttpServletRequest;

import org.cotrix.security.Token;
import org.cotrix.security.TokenCollector;
import org.cotrix.security.tokens.NameAndPassword;

public class DefaultNameAndPasswordCollector implements TokenCollector {

	public static final String nameParam="name";
	public static final String pwdParam="pwd";
	
	@Override
	public Token token(HttpServletRequest request) {
		
		String name = (String) request.getAttribute(nameParam);
		String pwd = (String) request.getAttribute(pwdParam);
		
		return name!=null && pwd!=null? new NameAndPassword(name,pwd):null;
	}
}
