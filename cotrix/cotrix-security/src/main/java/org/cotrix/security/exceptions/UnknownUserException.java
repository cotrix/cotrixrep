package org.cotrix.security.exceptions;

public class UnknownUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public UnknownUserException(String msg) {
		super(msg);
	}
	
}
