package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.UIUser;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

class UserSuggestion implements Suggestion {
	
	protected UIUser user;

	/**
	 * @param user
	 */
	public UserSuggestion(UIUser user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public UIUser getUser() {
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