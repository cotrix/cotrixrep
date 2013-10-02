package org.cotrix.security;

import javax.servlet.http.HttpServletRequest;

import org.cotrix.user.User;

public interface LoginService {

	User login(HttpServletRequest request);
}
