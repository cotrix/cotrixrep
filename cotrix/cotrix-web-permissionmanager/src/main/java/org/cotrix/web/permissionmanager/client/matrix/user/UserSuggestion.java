package org.cotrix.web.permissionmanager.client.matrix.user;

import org.cotrix.web.share.shared.UIUser;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserSuggestion implements Suggestion {
	
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
		return user.getFullName();
	}

	@Override
	public String getReplacementString() {
		return user.getFullName();
	}
	
}