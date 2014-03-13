package org.cotrix.security;

public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public InvalidCredentialsException() {
		super("unknown user or invalid credentials");
	}
	
}
