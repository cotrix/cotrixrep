/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.event.UserLoggedEvent;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.client.view.UserBarView.Presenter;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.HasFeature;
import org.cotrix.web.share.shared.feature.AuthenticationFeature;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserBarPresenterImpl implements Presenter, UserBarPresenter, LoginDialogListener {
	
	protected LoginDialog loginDialog = new LoginDialog(this);
	
	protected UserBarView view;
	
	protected EventBus cotrixBus;
	
	@Inject
	public UserBarPresenterImpl(UserBarView view, @CotrixBus EventBus cotrixBus)
	{
		this.view = view;
		this.cotrixBus = cotrixBus;
		view.setPresenter(this);
		bindFeatures();
		bind();
	}
	
	protected void bind()
	{
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {
			
			@Override
			public void onUserLogged(UserLoggedEvent event) {
				view.setUsername(event.getUsername());
			}
		});
		
		cotrixBus.addHandler(StatusUpdatedEvent.TYPE, new StatusUpdatedEvent.StatusUpdatedHandler() {
			
			@Override
			public void onStatusUpdated(StatusUpdatedEvent event) {
				String status = event.getStatus();
				view.setStatus(status!=null?status:"");
			}
		});
	}
		
	protected void bindFeatures()
	{
		FeatureBinder.bind(new HasFeature() {
			
			@Override
			public void unsetFeature() {
				view.setLoginVisible(false);
			}
			
			@Override
			public void setFeature() {
				view.setLoginVisible(true);
			}
		}, AuthenticationFeature.CAN_LOGIN);
		
		FeatureBinder.bind(new HasFeature() {
			
			@Override
			public void unsetFeature() {
				view.setLogoutVisible(false);
			}
			
			@Override
			public void setFeature() {
				view.setLogoutVisible(true);
			}
		}, AuthenticationFeature.CAN_LOGOUT);
	}

	@Override
	public void onLoginClick() {
		loginDialog.center();
	}

	@Override
	public void onLogoutClick() {
		cotrixBus.fireEvent(new UserLogoutEvent());
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());		
	}

	@Override
	public void onLogin(String username, String password) {
		loginDialog.hide();
		cotrixBus.fireEvent(new UserLoginEvent(username, password));
	}

	@Override
	public void onCancel() {
		loginDialog.hide();
	}

}
