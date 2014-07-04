/**
 * 
 */
package org.cotrix.web.common.client.feature;

import org.cotrix.web.common.client.feature.InstanceFeatureBind.IdProvider;
import org.cotrix.web.common.shared.UIUser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserProvider implements IdProvider {
	
	private UIUser user;

	@Override
	public String getId() {
		return user!=null?user.getId():null;
	}
	
	public UIUser getUser() {
		return user;
	}

	public void setUser(UIUser user) {
		this.user = user;
	}
}
