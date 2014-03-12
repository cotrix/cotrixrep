package org.cotrix.security;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class InvalidUsernameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public InvalidUsernameException(String username) {
		super("a user with username '"+username+"' has already signed up");
	}
	
}
