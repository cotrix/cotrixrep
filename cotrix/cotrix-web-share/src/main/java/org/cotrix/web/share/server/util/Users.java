/**
 * 
 */
package org.cotrix.web.share.server.util;

import org.cotrix.domain.user.User;
import org.cotrix.web.share.shared.UIUser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Users {
	
	public static UIUser toUiUser(User user) {
		return new UIUser(user.id(), user.name(), user.fullName());
	}

}
