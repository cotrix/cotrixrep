/**
 * 
 */
package org.cotrix.web.permissionmanager.client.profile;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.shared.UIUserDetails;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.util.StatusUpdates;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ProfilePanel extends ResizeComposite {

	interface ProfilePanelUiBinder extends UiBinder<Widget, ProfilePanel> {
	}
	
	@UiField Label username;
	@UiField TextBox fullname;
	@UiField TextBox email;
	@UiField PasswordTextBox password;
	
	@Inject
	protected PermissionServiceAsync service;

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Inject
	protected void bind(@CotrixBus EventBus cotrixBus) {
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {
			
			@Override
			public void onUserLogged(UserLoggedEvent event) {
				updateUserProfile();
			}
		});
	}
	
	@UiHandler({"fullname", "email"})
	protected void onBlur(BlurEvent event) {
		
		StatusUpdates.statusSaving();
		service.saveUserDetails(getUserDetails(), new ManagedFailureCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				StatusUpdates.statusSaved();
			}
		});
	}
	
	protected void updateUserProfile() {
		service.getUserDetails(new ManagedFailureCallback<UIUserDetails>() {

			@Override
			public void onSuccess(UIUserDetails result) {
				setUserDetails(result);				
			}
		});
	}
	
	protected void setUserDetails(UIUserDetails user) {
		username.setText(user.getUsername());
		fullname.setText(user.getFullName());
		email.setText(user.getEmail());
	}
	
	protected UIUserDetails getUserDetails() {
		UIUserDetails userDetails = new UIUserDetails();
		userDetails.setUsername(username.getText());
		userDetails.setFullName(fullname.getText());
		userDetails.setEmail(email.getText());
		userDetails.setPassword(password.getText());
		return userDetails;
	}
}
