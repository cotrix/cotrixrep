/**
 * 
 */
package org.cotrix.web.permissionmanager.client.profile;

import org.cotrix.web.permissionmanager.client.ModuleActivactedEvent;
import org.cotrix.web.permissionmanager.client.PermissionBus;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.profile.PasswordUpdateDialog.PassworUpdatedEvent;
import org.cotrix.web.permissionmanager.client.profile.PasswordUpdateDialog.PasswordUpdatedHandler;
import org.cotrix.web.permissionmanager.shared.PermissionUIFeatures;
import org.cotrix.web.permissionmanager.shared.UIUserDetails;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.HasVisibleFeature;
import org.cotrix.web.share.client.feature.ValueBoxEditing;
import org.cotrix.web.share.client.feature.InstanceFeatureBind.IdProvider;
import org.cotrix.web.share.client.util.AccountValidator;
import org.cotrix.web.share.client.util.StatusUpdates;
import org.cotrix.web.share.client.widgets.LoadingPanel;
import org.cotrix.web.share.shared.UIUser;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
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
public class ProfilePanel extends LoadingPanel {

	interface ProfilePanelUiBinder extends UiBinder<Widget, ProfilePanel> {}
	interface ProfilePanelEventBinder extends EventBinder<ProfilePanel> {}

	protected interface Style extends CssResource {
		String invalidValue();
	}

	@UiField Label username;
	@UiField TextBox fullname;
	@UiField TextBox email;
	
	@UiField Button password;
	
	@Inject PasswordUpdateDialog passwordUpdateDialog;

	@UiField Style style;

	@Inject
	protected PermissionServiceAsync service;
	protected UIUserDetails userDetails = new UIUserDetails();
	
	@Inject
	protected UserIdProvider userIdProvider;	

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		setAnimated(true);
		passwordUpdateDialog.addPasswordUpdateHandler(new PasswordUpdatedHandler() {
			
			@Override
			public void onAddUser(PassworUpdatedEvent event) {
				StatusUpdates.statusSaving();
				service.saveUserPassword(userDetails.getId(), event.getPassword(), new ManagedFailureCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						StatusUpdates.statusSaved();
					}
				});
			}
		});
		
		FeatureBinder.bind(new ValueBoxEditing<String>(fullname), userIdProvider, PermissionUIFeatures.EDIT_PROFILE);
		FeatureBinder.bind(new ValueBoxEditing<String>(email), userIdProvider, PermissionUIFeatures.EDIT_PROFILE);
		FeatureBinder.bind(new HasVisibleFeature(password), userIdProvider, PermissionUIFeatures.CHANGE_PASSWORD);
	}
	
	@Inject
	protected void bind(@PermissionBus EventBus bus, ProfilePanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}
	
	@EventHandler
	protected void onModuleActivated(ModuleActivactedEvent event) {
		updateUserProfile(userIdProvider.getId());
	}
	
	@UiHandler("password")
	protected void onPasswordChange(ClickEvent event) {
		passwordUpdateDialog.center();
	}
	
	@UiHandler({"fullname","email"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(style.invalidValue(), false);
		 }
	}

	@UiHandler({"fullname", "email"})
	protected void onBlur(BlurEvent event) {
		if (fullname.isReadOnly() && email.isReadOnly()) return;
		
		boolean valid = validate();
		if (valid) {
			StatusUpdates.statusSaving();
			service.saveUserDetails(getUserDetails(), new ManagedFailureCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					StatusUpdates.statusSaved();
				}
			});
		}
	}

	protected boolean validate() {
		boolean valid = true;
		
		if (!AccountValidator.validateFullName(fullname.getText())) {
			fullname.setStyleName(style.invalidValue(), true);
			valid = false;
		}

		if (!AccountValidator.validateEMail(email.getText())) {
			email.setStyleName(style.invalidValue(), true);
			valid = false;
		}
		return valid;
	}

	protected void updateUserProfile(String userId) {

		showLoader();
		service.getUserDetails(userId, new ManagedFailureCallback<UIUserDetails>() {

			@Override
			public void onSuccess(UIUserDetails result) {
				setUserDetails(result);
				hideLoader();
			}
		});
	}

	protected void setUserDetails(UIUserDetails userDetails) {
		Log.trace("setUserDetails "+userDetails);
		this.userDetails = userDetails;
		username.setText(userDetails.getUsername());
		fullname.setText(userDetails.getFullName());
		email.setText(userDetails.getEmail());
	}

	protected UIUserDetails getUserDetails() {
		userDetails.setUsername(username.getText());
		userDetails.setFullName(fullname.getText());
		userDetails.setEmail(email.getText());
		return userDetails;
	}
	
	@Singleton
	public static class UserIdProvider implements IdProvider, UserLoggedEvent.UserLoggedHandler {
		
		protected UIUser user;
		
		@Inject
		protected void init(@CotrixBus EventBus cotrixBus) {
			cotrixBus.addHandler(UserLoggedEvent.TYPE, this);
		}

		@Override
		public void onUserLogged(UserLoggedEvent event) {
			this.user = event.getUser();
		}

		@Override
		public String getId() {
			return user!=null?user.getId():null;
		}
		
	}
}
