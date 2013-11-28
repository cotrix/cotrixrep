/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.user;

import org.cotrix.web.permissionmanager.shared.UIUser;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserAddedEvent extends GenericEvent {
	
	protected UIUser user;

	/**
	 * @param user
	 */
	public UserAddedEvent(UIUser user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public UIUser getUser() {
		return user;
	}

}
