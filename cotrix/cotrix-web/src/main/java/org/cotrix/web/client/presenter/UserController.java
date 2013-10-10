/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.event.UserLoggedEvent;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLoginEvent.UserLoginHandler;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.shared.feature.Response;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserController {
	
	protected static final String GUEST_USERNAME = null;
	protected static final String GUEST_PASSWORD = null;
	
	protected EventBus cotrixBus;
	
	protected AsyncCallback<Response<String>> callback = FeatureAsyncCallBack.wrap(new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			Log.error("Login failed", caught);
		}

		@Override
		public void onSuccess(String result) {
			cotrixBus.fireEvent(new UserLoggedEvent(result));
		}
	});
	
	@Inject
	protected MainServiceAsync service;
	
	@Inject
	public UserController(@CotrixBus EventBus cotrixBus)
	{
		this.cotrixBus = cotrixBus;
		
		bind();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				logGuest();
			}
		});		
	}

	protected void bind()
	{
		cotrixBus.addHandler(UserLoginEvent.TYPE, new UserLoginHandler() {
			
			@Override
			public void onUserLogin(UserLoginEvent event) {
				logUser(event.getUsername(), event.getPassword());
			}
		});
		cotrixBus.addHandler(UserLogoutEvent.TYPE, new UserLogoutEvent.UserLogoutHandler() {
			
			@Override
			public void onUserLogout(UserLogoutEvent event) {
				logGuest();
			}
		});
	}
	
	
	protected void logGuest()
	{
		service.login(GUEST_USERNAME, GUEST_PASSWORD, callback);
	}
	
	protected void logUser(String username, String password)
	{
		service.login(username, password, callback);
	}

}
