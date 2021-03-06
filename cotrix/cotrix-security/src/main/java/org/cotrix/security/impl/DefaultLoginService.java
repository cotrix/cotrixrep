package org.cotrix.security.impl;

import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.UserQueries.*;

import java.util.Iterator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.InvalidCredentialsException;
import org.cotrix.security.LoginRequest;
import org.cotrix.security.LoginService;
import org.cotrix.security.Realm;
import org.cotrix.security.TokenCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DefaultLoginService implements LoginService {
	
	private static Logger log = LoggerFactory.getLogger(LoginService.class);
	
	@Inject
	private Instance<TokenCollector> collectors;
	
	@Inject @Any
	private Instance<Realm> realms;
	
	@Inject
	private UserRepository users;
	
	@Inject @Current
	private BeanSession session;
	
	
	@Override
	public User login(LoginRequest request) {
	
		Object token = collectTokenFrom(request);
		
		User user = null;
		
		//no credentials provided, it's a guest
		if (token==null)
			user = guest;
		else {
						
			//from credentials to an identity
			String identity = identifyFrom(token);
			
			if (identity==null)
				throw new InvalidCredentialsException();
			
			//from identity to currentUser
			user = users.get(userByName(identity));
			
			if (user==null)
				throw new InvalidCredentialsException();
		
			
			//log only for non-guests
			log.info("user {} has logged in",user.name());
		}
		
		//remember for this session
		session.add(User.class,user);

		return user;
	}
	
	//helpers
	private Object collectTokenFrom(LoginRequest request) {
	
		Object token =null;

		Iterator<TokenCollector> it = collectors.iterator();
		
		while (it.hasNext() && token==null) 
			token = it.next().token(request);
		
		return token;
	}
	
	private String identifyFrom(Object token) {
		
		Iterator<Realm> it = realms.iterator();
		
		String id = null;
		
		search: while (it.hasNext() && id==null) {
			
			Realm authenticator =it.next();
			
			if (authenticator.supports(token)) {
				id = authenticator.login(token);
				if (id!=null)
					break search;
			}
		}
		
		return id;
	}	

}
