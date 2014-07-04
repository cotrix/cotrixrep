/**
 * 
 */
package org.cotrix.web.users.client.profile;

import org.cotrix.web.common.client.error.ErrorManager;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.HasVisibleFeature;
import org.cotrix.web.common.client.feature.UserProvider;
import org.cotrix.web.common.client.feature.ValueBoxEditing;
import org.cotrix.web.common.client.util.AccountValidator;
import org.cotrix.web.common.client.util.StatusUpdates;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.users.client.ModuleActivactedEvent;
import org.cotrix.web.users.client.UsersBus;
import org.cotrix.web.users.client.UsersServiceAsync;
import org.cotrix.web.users.client.profile.PasswordUpdateDialog.PasswordUpdateListener;
import org.cotrix.web.users.shared.InvalidPasswordException;
import org.cotrix.web.users.shared.PermissionUIFeatures;
import org.cotrix.web.users.shared.UIUserDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private AlertDialog alertDialog;
	
	@Inject
	private ErrorManager errorManager;

	@Inject
	private UsersServiceAsync service;
	private UIUserDetails userDetails = new UIUserDetails();
	
	@Inject
	private UserProvider userIdProvider;	
	
	@Inject
	private FeatureBinder featureBinder;

	@Inject
	protected void init(ProfilePanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		setAnimated(true);
		passwordUpdateDialog.setListener(new PasswordUpdateListener() {
			
			@Override
			public void onPasswordUpdate(String oldPassword, String newPassword) {
				StatusUpdates.statusSaving();
				service.updateUserPassword(userDetails.getId(), oldPassword, newPassword, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("Password update failed", caught);
						if (caught instanceof InvalidPasswordException) alertDialog.center("Invalid credentials.");
						else errorManager.showError(Exceptions.toError(caught));
					}

					@Override
					public void onSuccess(Void result) {
						StatusUpdates.statusSaved();
					}
				});
			}
		});
		
		featureBinder.bind(new ValueBoxEditing<String>(fullname), userIdProvider, PermissionUIFeatures.EDIT_PROFILE);
		featureBinder.bind(new ValueBoxEditing<String>(email), userIdProvider, PermissionUIFeatures.EDIT_PROFILE);
		featureBinder.bind(new HasVisibleFeature(password), userIdProvider, PermissionUIFeatures.CHANGE_PASSWORD);
	}
	
	@Inject
	protected void bind(@UsersBus EventBus bus, ProfilePanelEventBinder eventBinder) {
		eventBinder.bindEventHandlers(this, bus);
	}
	
	@EventHandler
	protected void onModuleActivated(ModuleActivactedEvent event) {
		updateUserProfile(userIdProvider.getId());
	}
	
	@UiHandler("password")
	protected void onPasswordChange(ClickEvent event) {
		passwordUpdateDialog.clean();
		passwordUpdateDialog.showCentered();
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
}
