package org.cotrix.security.exceptions;

public class UnauthorizedUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public UnauthorizedUserException(String msg) {
		super(msg);
	}
	
}
