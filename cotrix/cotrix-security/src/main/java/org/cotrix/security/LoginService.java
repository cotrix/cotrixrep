package org.cotrix.security;

import org.cotrix.domain.user.User;

public interface LoginService {

	User login(LoginRequest request);
}
