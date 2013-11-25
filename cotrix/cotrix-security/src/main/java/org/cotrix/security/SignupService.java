package org.cotrix.security;

import org.cotrix.domain.user.User;

public interface SignupService {
	
	void signup(User user, String pwd);

}
