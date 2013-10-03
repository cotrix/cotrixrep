/**
 * 
 */
package org.cotrix.web.client.presenter;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.client.view.UserBarView.Presenter;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.HasFeature;
import org.cotrix.web.share.shared.feature.Response;
import org.cotrix.web.shared.AuthenticationFeature;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserBarPresenterImpl implements Presenter, UserBarPresenter, LoginDialogListener {
	
	protected static final String GUEST_USERNAME = null;
	protected static final String GUEST_PASSWORD = null;
	
	protected MainServiceAsync service;
	
	protected UserBarView view;
	
	protected LoginDialog loginDialog = new LoginDialog(this);
	
	protected AsyncCallback<Response<String>> callback = FeatureAsyncCallBack.wrap(new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(String result) {
			view.setUsername(result);
		}
	});
	
	@Inject
	public UserBarPresenterImpl(MainServiceAsync service, UserBarView view)
	{
		this.service = service;
		this.view = view;
		view.setPresenter(this);
		bindFeatures();
		
		logGuest();
	}
	
	protected void logGuest()
	{
		//TODO right position?
		service.login(GUEST_USERNAME, GUEST_PASSWORD, callback);
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
		logGuest();
		//service.logout(FeatureAsyncCallBack.<Void>nop());
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());		
	}

	@Override
	public void onLogin(String username, String password) {
		loginDialog.hide();
		service.login(username, password, callback);
	}

	@Override
	public void onCancel() {
		loginDialog.hide();
	}

}
