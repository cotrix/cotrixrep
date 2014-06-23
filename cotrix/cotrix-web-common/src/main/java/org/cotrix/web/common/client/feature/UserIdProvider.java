/**
 * 
 */
package org.cotrix.web.common.client.feature;

import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.feature.InstanceFeatureBind.IdProvider;
import org.cotrix.web.common.shared.UIUser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserIdProvider implements IdProvider, UserLoggedEvent.UserLoggedHandler {
	
	private UIUser user;
	
	@Inject
	protected void init(@CotrixBus EventBus cotrixBus) {
		cotrixBus.addHandler(UserLoggedEvent.TYPE, this);
	}

	@Override
	public void onUserLogged(UserLoggedEvent event) {
		this.user = event.getUser();
	}

	@Override
	public String getId() {
		return user!=null?user.getId():null;
	}
}
