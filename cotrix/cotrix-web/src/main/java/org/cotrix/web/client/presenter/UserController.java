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
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CodelistClosedEvent;
import org.cotrix.web.share.client.event.CodelistOpenedEvent;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;
import org.cotrix.web.share.client.feature.AsyncCallBackWrapper;
import org.cotrix.web.share.client.rpc.Nop;
import org.cotrix.web.share.client.util.Exceptions;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
import org.cotrix.web.shared.UnknownUserException;

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
	
	protected static final String GUEST_USERNAME = null; //"cotrix";
	protected static final String GUEST_PASSWORD = null; //"cotrix";
	
	protected EventBus cotrixBus;
	protected List<String> openedCodelists = new ArrayList<String>();
	
	protected AsyncCallback<ResponseWrapper<String>> loginCallback = AsyncCallBackWrapper.wrap(new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			Log.error("Login failed", caught);
			if (caught instanceof UnknownUserException) {
				AlertDialog.INSTANCE.center("Unknown user please check you credentials and re-try.", Exceptions.getPrintStackTrace(caught));
			}
		}

		@Override
		public void onSuccess(String result) {
			cotrixBus.fireEvent(new UserLoggedEvent(result));
		}
	});
	
	protected AsyncCallback<ResponseWrapper<String>> logoutCallback = AsyncCallBackWrapper.wrap(new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			Log.error("Login failed", caught);
		}

		@Override
		public void onSuccess(String result) {
			cotrixBus.fireEvent(new UserLoggedEvent(result));
			cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.HOME));
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
		cotrixBus.addHandler(UserRegisterEvent.TYPE, new UserRegisterEvent.UserRegisterHandler() {
			
			@Override
			public void onUserRegister(UserRegisterEvent event) {
				registerUser(event.getUsername(), event.getPassword(), event.getEmail());
			}
		});
	}
	
	
	protected void logGuest()
	{
		service.login(GUEST_USERNAME, GUEST_PASSWORD, openedCodelists, loginCallback);
	}
	
	protected void logout()
	{
		service.logout(openedCodelists, logoutCallback);
	}
	
	protected void logUser(String username, String password)
	{
		service.login(username, password, openedCodelists, loginCallback);
	}
	
	protected void registerUser(String username, String password, String email)
	{
		service.registerUser(username, password, email, Nop.<ResponseWrapper<String>>getInstance());
	}

}
