/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix.UsersRolesMatrixListener;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.share.client.error.ManagedFailureCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ApplicationPermissionPanel extends ResizeComposite {

	private static ApplicationPermissionPanelUiBinder uiBinder = GWT
			.create(ApplicationPermissionPanelUiBinder.class);

	interface ApplicationPermissionPanelUiBinder extends
			UiBinder<Widget, ApplicationPermissionPanel> {
	}
	
	protected PermissionServiceAsync service = GWT.create(PermissionService.class);
	
	@UiField(provided=true) UsersRolesMatrix usersRolesMatrix;

	public ApplicationPermissionPanel() {
		setupMatrix();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void setupMatrix() {
		usersRolesMatrix = new UsersRolesMatrix(new UsersRolesMatrixListener() {
			
			@Override
			public void onRolesRowUpdated(RolesRow row) {
				
				service.applicationRolesRowUpdated(row, new ManagedFailureCallback<Void>() {
					
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
