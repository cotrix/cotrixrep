package org.cotrix.security;

/**
 * An authentication token.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Token {

	public static Token guest = new Token() {};
}
