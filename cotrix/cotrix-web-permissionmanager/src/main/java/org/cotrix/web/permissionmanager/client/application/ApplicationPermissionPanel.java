/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix.UsersRolesMatrixListener;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.util.StatusUpdates;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ApplicationPermissionPanel extends ResizeComposite {

	interface ApplicationPermissionPanelUiBinder extends
	UiBinder<Widget, ApplicationPermissionPanel> {
	}

	@Inject
	protected PermissionServiceAsync service;

	@Inject @UiField(provided=true) UsersRolesMatrix usersRolesMatrix;

	@Inject
	protected void init(ApplicationPermissionPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Inject
	protected void bind(@CotrixBus EventBus cotrixBus) {
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {
			
			@Override
			public void onUserLogged(UserLoggedEvent event) {
				usersRolesMatrix.refresh();
			}
		});
	}

	@Inject
	protected void setupMatrix(final ApplicationRolesRowDataProvider applicationRolesRowDataProvider) {
		usersRolesMatrix.setListener(new UsersRolesMatrixListener() {

			@Override
			public void onRolesRowUpdated(RolesRow row, String role, boolean value) {

				RoleAction action = value?RoleAction.DELEGATE:RoleAction.REVOKE;
				StatusUpdates.statusSaving();
				service.applicationRoleUpdated(row.getUser().getId(), role, action, new ManagedFailureCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						StatusUpdates.statusSaved();
					}
				});

			}
		});
		service.getRoles(RolesType.APPLICATION, new ManagedFailureCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				usersRolesMatrix.setupMatrix(result, applicationRolesRowDataProvider);
			}
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) onResize();
	}

}
