package org.cotrix.security;

/**
 * A realm of currentUser identities.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type authentication {@link Token} that identifies users in this realm.
 */
public interface Realm<T extends Token> {

	/**
	 * Returns <code>true</code> if this realm support an authentication token of a given type.
	 * 
	 * @param token a token of the given type
	 * @return <code>true</code> if this realm support the token of the given type
	 */
	boolean supports(Token token);

	/**
	 * Returns the identity associated with a given token in this realm, or <code>null</code> if no identity is
	 * associated with the token.
	 * 
	 * @param token the token
	 * @return the identity associated with the token in this realm, or <code>null</code> if no identity is associated
	 *         with the token
	 */
	String login(T token);
}
