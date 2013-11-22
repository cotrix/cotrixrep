package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.User;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

class UserSuggestion implements Suggestion {
	
	protected User user;

	/**
	 * @param user
	 */
	public UserSuggestion(User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	@Override
	public String getDisplayString() {
		return user.getUsername();
	}

	@Override
	public String getReplacementString() {
		return user.getUsername();
	}
	
}