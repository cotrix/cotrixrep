/**
 * 
 */
package org.cotrix.web.users.client.application;

import java.util.List;

import org.cotrix.web.users.client.PermissionServiceAsync;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.util.StatusUpdates;
import org.cotrix.web.common.client.widgets.ConfirmDialog;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ConfirmDialog.DialogButton;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.users.client.PermissionBus;
import org.cotrix.web.users.client.matrix.RolesRowUpdatedEvent;
import org.cotrix.web.users.client.matrix.UsersRolesMatrix;
import org.cotrix.web.users.shared.RoleAction;
import org.cotrix.web.users.shared.RolesRow;
import org.cotrix.web.users.shared.RolesType;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
	@UiField ItemToolbar toolBar;

	@Inject
	protected ApplicationRolesRowDataProvider dataProvider;

	@Inject
	protected void init(ApplicationPermissionPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		toolBar.setVisible(ItemButton.PLUS, false);
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

	@UiHandler("toolBar")
	protected void onToolBarButtonClicked(ButtonClickedEvent event) {
		if (event.getButton() == ItemButton.MINUS) removeSelectedUser();
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
		
		confirmDialog.center("If you revoke this role, "+row.getUser().getFullName()+" will have none left and the account will be closed. Do you want to go ahead?");
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

	protected void removeSelectedUser() {
		final RolesRow row = usersRolesMatrix.getSelectedRow();
		if (row!=null) {
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.addSelectionHandler(new SelectionHandler<ConfirmDialog.DialogButton>() {

				@Override
				public void onSelection(SelectionEvent<DialogButton> event) {
					if (event.getSelectedItem() == DialogButton.CONTINUE) {
						removeUser(row.getUser().getId());
					}
				}

			});
			
			confirmDialog.center( "This will close "+row.getUser().getFullName()+"'s account. Do you want to go ahead?");
		}
	}

	protected void removeUser(String id) {
		service.removeUser(id, new ManagedFailureCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				usersRolesMatrix.refresh();
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
