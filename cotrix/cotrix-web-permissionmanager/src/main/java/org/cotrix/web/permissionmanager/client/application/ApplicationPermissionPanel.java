/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix.UsersRolesMatrixListener;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.share.client.error.ManagedFailureCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ApplicationPermissionPanel extends ResizeComposite {

	interface ApplicationPermissionPanelUiBinder extends
	UiBinder<Widget, ApplicationPermissionPanel> {
	}

	protected PermissionServiceAsync service = GWT.create(PermissionService.class);

	@Inject @UiField(provided=true) UsersRolesMatrix usersRolesMatrix;

	@Inject
	protected void init(ApplicationPermissionPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Inject
	protected void setupMatrix() {
		usersRolesMatrix.setListener(new UsersRolesMatrixListener() {

			@Override
			public void onRolesRowUpdated(RolesRow row, String role, boolean value) {



				RoleAction action = value?RoleAction.DELEGATE:RoleAction.REVOKE;
				service.applicationRoleUpdated(row.getUser().getId(), role, action, new ManagedFailureCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub

					}
				});

			}
		});
		service.getRoles(RolesType.APPLICATION, new ManagedFailureCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				usersRolesMatrix.setupMatrix(result, new ApplicationRolesRowDataProvider());
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
