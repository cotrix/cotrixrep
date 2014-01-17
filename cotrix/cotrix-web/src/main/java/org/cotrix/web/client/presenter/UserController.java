/**
 * 
 */
package org.cotrix.web.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLoginEvent.UserLoginHandler;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CodelistClosedEvent;
import org.cotrix.web.share.client.event.CodelistOpenedEvent;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.util.Exceptions;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.shared.UIUser;
import org.cotrix.web.shared.SessionIdToken;
import org.cotrix.web.shared.UnknownUserException;
import org.cotrix.web.shared.UsernamePasswordToken;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserController {
	
	protected EventBus cotrixBus;
	protected List<String> openedCodelists = new ArrayList<String>();
	
	protected AsyncCallback<UIUser> loginCallback = new AsyncCallback<UIUser>() {

		@Override
		public void onFailure(Throwable caught) {
			Log.error("Login failed", caught);
			if (caught instanceof UnknownUserException) {
				AlertDialog.INSTANCE.center("Unknown user please check your credentials and re-try.", Exceptions.getPrintStackTrace(caught));
			}
		}

		@Override
		public void onSuccess(UIUser result) {
			cotrixBus.fireEvent(new UserLoggedEvent(result));
		}
	};
	
	protected AsyncCallback<UIUser> logoutCallback = new AsyncCallback<UIUser>() {

		@Override
		public void onFailure(Throwable caught) {
			Log.error("Login failed", caught);
		}

		@Override
		public void onSuccess(UIUser result) {
			cotrixBus.fireEvent(new UserLoggedEvent(result));
			cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.HOME));
		}
	};
	
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
				initialLogin();
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
	
	protected void initialLogin() {
		String sessionIdParameter = Location.getParameter("sessionId");
		Log.trace("sessionIdParameter: "+sessionIdParameter);
		if (sessionIdParameter == null) getCurrentUser();
		else logUsingSessionId(sessionIdParameter);
	}
	
	
	protected void getCurrentUser()
	{
		service.getCurrentUser(loginCallback);
		//service.login(UsernamePasswordToken.GUEST, openedCodelists, loginCallback);
	}
	
	protected void logUsingSessionId(String sessionId) {
		service.login(new SessionIdToken(sessionId), openedCodelists, loginCallback);
	}
	
	protected void logout()
	{
		service.logout(openedCodelists, logoutCallback);
	}
	
	protected void logUser(String username, String password)
	{
		service.login(new UsernamePasswordToken(username, password), openedCodelists, loginCallback);
	}
	
	protected void registerUser(String username, String password, String email)
	{
		service.registerUser(username, password, email, openedCodelists, loginCallback);
	}

}
