/**
 * 
 */
package org.cotrix.web.client.userbar;

import org.cotrix.web.client.dialog.LoginDialog;
import org.cotrix.web.client.dialog.RegisterDialog;
import org.cotrix.web.client.dialog.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.dialog.RegisterDialog.RegisterDialogListener;
import org.cotrix.web.client.event.UserLoggingInEvent;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLoginFailedEvent;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.client.event.UserRegisteringEvent;
import org.cotrix.web.client.event.UserRegistrationFailedEvent;
import org.cotrix.web.client.userbar.UserBarView.Presenter;
import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.ExtensibleComponentReadyEvent;
import org.cotrix.web.common.client.event.StatusUpdatedEvent;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.feature.ApplicationFeatures;
import org.cotrix.web.shared.AuthenticationFeature;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserBarPresenter implements Presenter, LoginDialogListener, RegisterDialogListener {

	protected static interface UserBarPresenterEventBinder extends EventBinder<UserBarPresenter> {}

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
	public UserBarPresenter(UserBarView view)
	{
		this.view = view;
		view.setPresenter(this);
	}
	
	@Inject
	private void extensionSetup(){
		cotrixBus.fireEvent(new ExtensibleComponentReadyEvent(UserBarView.NAME, view));
	}

	@Inject
	private void bind(UserBarPresenterEventBinder binder) {
		binder.bindEventHandlers(this, cotrixBus);
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
	}

	@EventHandler
	void onUserLoggingIn(UserLoggingInEvent event) {
		view.setUserLoading(true);
	}

	@EventHandler
	void onUserRegistering(UserRegisteringEvent event) {
		view.setUserLoading(true);
	}

	@EventHandler
	void onUserLoginFailed(UserLoginFailedEvent event) {
		view.setUserLoading(false);
		alertDialog.center("Unknown user please check your credentials and re-try.", event.getError().getDetails());
	}

	@EventHandler
	void onUserRegistrationFailed(UserRegistrationFailedEvent event) {
		view.setUserLoading(false);
		alertDialog.center(event.getError());
	}

	@Inject
	private void bindDialogs() {
		loginDialog.setListener(this);
		registerDialog.setListener(this);
	}

	@Override
	public void onRegister() {
		showRegisterDialog();
	}

	@Override
	public void onLogin(String username, String password) {
		loginDialog.hide();
		cotrixBus.fireEvent(new UserLoginEvent(username, password));
	}

	@Override
	public void onRegister(String username, String password, String email) {
		registerDialog.hide();
		cotrixBus.fireEvent(new UserRegisterEvent(username, password, email));
	}

	@Inject
	protected void bindFeatures(FeatureBinder featureBinder)
	{
		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				view.setLoginVisible(active);
			}
		}, AuthenticationFeature.CAN_LOGIN);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				view.setLogoutVisible(active);
			}

		}, AuthenticationFeature.CAN_LOGOUT);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				view.setRegisterVisible(active);
			}
		}, AuthenticationFeature.CAN_REGISTER);

		featureBinder.bind(new FeatureToggler() {

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
