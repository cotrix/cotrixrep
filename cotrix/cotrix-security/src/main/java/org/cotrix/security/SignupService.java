package org.cotrix.security;

import org.cotrix.domain.User;

public interface SignupService {
	
	void signup(User user, String pwd);

}
