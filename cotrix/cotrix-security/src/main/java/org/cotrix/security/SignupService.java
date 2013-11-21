package org.cotrix.security;

import org.cotrix.user.User;

public interface SignupService {
	
	void signup(User user, String pwd);

}
