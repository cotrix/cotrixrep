/**
 * 
 */
package org.cotrix.security.events;

import org.cotrix.domain.user.User;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SignupEvent {

	protected User user;

	/**
	 * @param username
	 */
	public SignupEvent(User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SignupEvent [user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
}
