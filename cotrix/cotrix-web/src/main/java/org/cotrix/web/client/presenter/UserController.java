/**
 * 
 */
package org.cotrix.web.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.event.UserLoggedEvent;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLoginEvent.UserLoginHandler;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.share.client.event.CodelistClosedEvent;
import org.cotrix.web.share.client.event.CodelistOpenedEvent;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.share.shared.feature.ResponseWrapper;

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
	
	protected static final String GUEST_USERNAME = "cotrix";
	protected static final String GUEST_PASSWORD = "cotrix";
	
	protected EventBus cotrixBus;
	protected List<String> openedCodelists = new ArrayList<String>();
	
	protected AsyncCallback<ResponseWrapper<String>> callback = AsyncCallBackWrapper.wrap(new AsyncCallback<String>() {

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
				logout();
			}
		});
		cotrixBus.addHandler(CodelistOpenedEvent.TYPE, new CodelistOpenedEvent.CodelistOpenedHandler() {
			
			@Override
			public void onCodelistOpened(CodelistOpenedEvent event) {
				openedCodelists.add(event.getCodelistId());
			}
		});
		cotrixBus.addHandler(CodelistClosedEvent.TYPE, new CodelistClosedEvent.CodelistClosedHandler() {
			
			@Override
			public void onCodelistClosed(CodelistClosedEvent event) {
				openedCodelists.remove(event.getCodelistid());
			}
		});
	}
	
	
	protected void logGuest()
	{
		service.login(GUEST_USERNAME, GUEST_PASSWORD, openedCodelists, callback);
	}
	
	protected void logout()
	{
		service.logout(openedCodelists, callback);
	}
	
	protected void logUser(String username, String password)
	{
		service.login(username, password, openedCodelists, callback);
	}

}
