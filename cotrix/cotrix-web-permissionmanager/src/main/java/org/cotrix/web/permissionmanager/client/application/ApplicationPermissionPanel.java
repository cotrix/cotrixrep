/**
 * 
 */
package org.cotrix.web.permissionmanager.client.application;

import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionBus;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.matrix.RolesRowUpdatedEvent;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.util.StatusUpdates;
import org.cotrix.web.share.client.widgets.ConfirmDialog;
import org.cotrix.web.share.client.widgets.ConfirmDialog.DialogButton;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
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
public class ApplicationPermissionPanel extends ResizeComposite {

	interface ApplicationPermissionPanelUiBinder extends UiBinder<Widget, ApplicationPermissionPanel> {}
	interface ApplicationPermissionPanelEventBinder extends EventBinder<ApplicationPermissionPanel> {}

	@Inject
	protected PermissionServiceAsync service;

	@Inject @UiField(provided=true) UsersRolesMatrix usersRolesMatrix;
	
	@Inject
	protected ApplicationRolesRowDataProvider dataProvider;

	@Inject
	protected void init(ApplicationPermissionPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Inject
	protected void bind(@CotrixBus EventBus cotrixBus, @PermissionBus EventBus bus, ApplicationPermissionPanelEventBinder binder) {
		binder.bindEventHandlers(this, bus);
		
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {
			
			@Override
			public void onUserLogged(UserLoggedEvent event) {
				usersRolesMatrix.refresh();
			}
		});
	}
	
	@EventHandler
	protected void onRolesRowUpdate(final RolesRowUpdatedEvent rowUpdatedEvent) {
		if (rowUpdatedEvent.getSource()!=usersRolesMatrix) return;
		
		final RoleAction action = rowUpdatedEvent.getValue()?RoleAction.DELEGATE:RoleAction.REVOKE;
		final RolesRow row = rowUpdatedEvent.getRow();
		final String role = rowUpdatedEvent.getRole();
		final int rowIndex = rowUpdatedEvent.getRowIndex();
		
		if (action == RoleAction.REVOKE && row.countActiveRoles() == 1) {
			requestConfirmAndUpdateRole(row, role, action, rowIndex);
		} else updateRole(row, role, action, rowIndex);	
	}
	
	protected void requestConfirmAndUpdateRole(final RolesRow row, final String role, final RoleAction action, final int rowIndex) {
		ConfirmDialog confirmDialog = new ConfirmDialog();
		confirmDialog.addSelectionHandler(new SelectionHandler<ConfirmDialog.DialogButton>() {
			
			@Override
			public void onSelection(SelectionEvent<DialogButton> event) {
				if (event.getSelectedItem() == DialogButton.CONTINUE) updateRole(row, role, action, rowIndex);
				else {
					row.setLoading(false);
					System.out.println("redraw "+rowIndex);
					usersRolesMatrix.redrawRow(rowIndex);
				}
			}
		});
		confirmDialog.center("Removing this role will eliminate the user, are you sure to continue?");
	}
	
	protected void updateRole(final RolesRow row, String role, RoleAction action, final int rowIndex) {
		StatusUpdates.statusSaving();
		
		service.applicationRoleUpdated(row.getUser().getId(), role, action, new ManagedFailureCallback<RolesRow>() {

			@Override
			public void onSuccess(RolesRow updatedRow) {
				if (updatedRow.isDeleted()) {
					usersRolesMatrix.refresh();
				} else {
					row.setRoles(updatedRow.getRoles());
					usersRolesMatrix.redrawRow(rowIndex);
				}
				StatusUpdates.statusSaved();
			}
		});
	}

	@Inject
	protected void setupMatrix(final ApplicationRolesRowDataProvider applicationRolesRowDataProvider) {
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
		//Fix GWT issue
		if (visible) onResize();
	}
}
