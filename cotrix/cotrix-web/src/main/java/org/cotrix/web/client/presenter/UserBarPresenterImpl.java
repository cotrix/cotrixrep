/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.RegisterDialog;
import org.cotrix.web.client.view.RegisterDialog.RegisterDialogListener;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.client.view.UserBarView.Presenter;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.FeatureToggler;
import org.cotrix.web.share.shared.feature.ApplicationFeatures;
import org.cotrix.web.shared.AuthenticationFeature;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserBarPresenterImpl implements Presenter, UserBarPresenter, LoginDialogListener, RegisterDialogListener {
	
	protected LoginDialog loginDialog = new LoginDialog(this);
	protected RegisterDialog registerDialog = new RegisterDialog(this);
	
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
				view.setUserEnabled(true);
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
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setLoginVisible(active);
			}
		}, AuthenticationFeature.CAN_LOGIN);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setLogoutVisible(active);
			}

		}, AuthenticationFeature.CAN_LOGOUT);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setRegisterVisible(active);
			}
		}, AuthenticationFeature.CAN_REGISTER);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				view.setUsernameClickEnabled(active);
			}
		}, ApplicationFeatures.ACCESS_ADMIN_AREA);
	}

	@Override
	public void onLoginClick() {
		loginDialog.clean();
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
		registerDialog.hide();
	}

	@Override
	public void onRegisterClick() {
		registerDialog.clean();
		registerDialog.center();
	}

	@Override
	public void onRegister(String username, String password, String email) {
		registerDialog.hide();
		cotrixBus.fireEvent(new UserRegisterEvent(username, password, email));
	}

	@Override
	public void onRegister() {
		registerDialog.clean();
		registerDialog.center();
	}

	@Override
	public void onUserClick() {
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.PERMISSION));
	}

}
