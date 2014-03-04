/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.event.UserLoggingInEvent;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLoginFailedEvent;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.client.event.UserRegisteringEvent;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.RegisterDialog;
import org.cotrix.web.client.view.RegisterDialog.RegisterDialogListener;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.StatusUpdatedEvent;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.shared.AuthenticationFeature;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserBarPresenterImpl implements UserBarPresenter{

	@Inject
	protected LoginDialog loginDialog;
	@Inject
	protected RegisterDialog registerDialog;
	@Inject
	protected AlertDialog alertDialog;

	protected UserBarView view;

	@Inject @CotrixBus
	protected EventBus cotrixBus;

	@Inject
	public UserBarPresenterImpl(UserBarView view)
	{
		this.view = view;
		view.setPresenter(this);
	}

	@Inject
	protected void bind()
	{
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {

			@Override
			public void onUserLogged(UserLoggedEvent event) {
				view.setUsername(event.getUser().getUsername());
				view.setUserEnabled(true);
				view.setUserLoading(false);
			}
		});

		cotrixBus.addHandler(StatusUpdatedEvent.TYPE, new StatusUpdatedEvent.StatusUpdatedHandler() {

			@Override
			public void onStatusUpdated(StatusUpdatedEvent event) {
				String status = event.getStatus();
				view.setStatus(status!=null?status:"");
			}
		});

		cotrixBus.addHandler(UserLoggingInEvent.TYPE, new UserLoggingInEvent.UserLoggingInEventHandler() {

			@Override
			public void onUserLoggingIn(UserLoggingInEvent event) {
				view.setUserLoading(true);
			}
		});

		cotrixBus.addHandler(UserRegisteringEvent.TYPE, new UserRegisteringEvent.UserRegisteringEventHandler() {

			@Override
			public void onUserRegistering(UserRegisteringEvent event) {
				view.setUserLoading(true);
			}
		});

		cotrixBus.addHandler(UserLoginFailedEvent.TYPE, new UserLoginFailedEvent.UserLoginFailedEventHandler() {

			@Override
			public void onUserLoginFailed(UserLoginFailedEvent event) {
				view.setUserLoading(false);
				alertDialog.center("Unknown user please check your credentials and re-try.", event.getDetails());
			}
		});
	}

	@Inject
	private void bindDialogs() {
		loginDialog.setListener(new LoginDialogListener() {

			@Override
			public void onRegister() {
				showRegisterDialog();
			}

			@Override
			public void onLogin(String username, String password) {
				loginDialog.hide();
				cotrixBus.fireEvent(new UserLoginEvent(username, password));
			}
		});
		registerDialog.setListener(new RegisterDialogListener() {
			
			@Override
			public void onRegister(String username, String password, String email) {
				registerDialog.hide();
				cotrixBus.fireEvent(new UserRegisterEvent(username, password, email));
			}
		});
		
	}

	@Inject
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
		showLoginDialog();
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
	public void onRegisterClick() {
		showRegisterDialog();
	}

	private void showRegisterDialog() {
		registerDialog.clean();
		registerDialog.showCentered();
	}

	private void showLoginDialog() {
		loginDialog.clean();
		loginDialog.showCentered();
	}

	@Override
	public void onUserClick() {
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.PERMISSION));
	}

}
