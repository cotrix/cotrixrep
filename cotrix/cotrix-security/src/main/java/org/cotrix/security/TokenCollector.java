package org.cotrix.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Extracts authentication {@link Token}s from requests.
 * 
 * @author Fabio Simeoni
 *
 */
public interface TokenCollector {

	/**
	 * Returns an authentication {@link Token} from a request, or <code>null</code> if the request does not carry one.
	 * @param request the request
	 * @return the authentication token, or <code>null</code> if the request does not carry one
	 */
	Token token(HttpServletRequest request);
}