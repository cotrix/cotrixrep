/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import java.util.HashMap;
import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionBus;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistSelectedEvent;
import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistsTreePanel;
import org.cotrix.web.permissionmanager.client.matrix.RolesRowUpdatedEvent;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.client.matrix.user.UserAddedEvent;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.CodelistVersion;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RoleState;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;
import org.cotrix.web.share.client.util.StatusUpdates;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
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
public class CodelistsPermissionsPanel extends ResizeComposite {

	interface CodelistsPermissionsPanelUiBinder extends	UiBinder<Widget, CodelistsPermissionsPanel> {}
	interface CodelistsPermissionsPanelEventBinder extends EventBinder<CodelistsPermissionsPanel> {}

	@Inject
	protected PermissionServiceAsync service;

	@UiField DeckLayoutPanel centralPanel;
	@UiField HTMLPanel blankPanel;
	@UiField DockLayoutPanel rolesPanel;
	@Inject @UiField(provided=true) UsersRolesMatrix usersRolesMatrix;
	@Inject @UiField(provided=true) CodelistsTreePanel codelistsTreePanel;

	protected String currentCodelistId = null;

	@Inject
	protected CodelistRolesRowDataProvider dataProvider;
	
	protected List<String> roles;

	@Inject
	protected void init(CodelistsPermissionsPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		centralPanel.showWidget(blankPanel);
	}

	@Inject
	protected void bind(@CotrixBus EventBus cotrixBus, @PermissionBus EventBus bus, CodelistsPermissionsPanelEventBinder binder) {
		binder.bindEventHandlers(this, bus);
		cotrixBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEvent.UserLoggedHandler() {

			@Override
			public void onUserLogged(UserLoggedEvent event) {
				currentCodelistId = null;
				centralPanel.showWidget(blankPanel);
				codelistsTreePanel.refresh();
			}
		});
	}

	@Inject
	protected void setupMatrix() {
		service.getRoles(RolesType.CODELISTS, new ManagedFailureCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				usersRolesMatrix.setupMatrix(result, dataProvider);
			}
		});
	}

	@EventHandler
	protected void onRolesRowUpdate(RolesRowUpdatedEvent event) {
		if (event.getSource()!=usersRolesMatrix) return;
		saveRow(event.getRow(), event.getRole(), event.getValue());
	}

	@EventHandler
	protected void onUserAdded(UserAddedEvent event) {
		Log.trace("onUserAdded "+event.getUser());

		List<RolesRow> cache = dataProvider.getCache();

		RolesRow row = new RolesRow(event.getUser(), new HashMap<String, RoleState>());
		saveRow(row, null, true);
		cache.add(cache.size()-1, row);
		dataProvider.refresh();
	}

	@EventHandler
	protected void onCodelistSelected(CodelistSelectedEvent event) {
		Log.trace("onCodelistSelected "+event.getCodelist());
		showMatrix(event.getCodelist());
	}

	protected void showMatrix(CodelistVersion codelist) {
		Log.trace("showMatrix "+codelist);
		centralPanel.showWidget(rolesPanel);

		currentCodelistId = codelist.getId();
		dataProvider.setCodelistId(currentCodelistId);
		usersRolesMatrix.refresh();
	}

	protected void saveRow(final RolesRow row, String role, boolean value) {

		RoleAction action = value?RoleAction.DELEGATE:RoleAction.REVOKE;
		StatusUpdates.statusSaving();
		service.codelistRoleUpdated(row.getUser().getId(), currentCodelistId, role, action, new ManagedFailureCallback<RolesRow>() {

			@Override
			public void onSuccess(RolesRow updatedRow) {
				row.setRoles(updatedRow.getRoles());
				dataProvider.refresh();
				StatusUpdates.statusSaved();
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
