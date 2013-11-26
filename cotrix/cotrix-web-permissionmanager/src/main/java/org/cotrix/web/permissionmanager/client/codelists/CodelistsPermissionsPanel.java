/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.client.PermissionServiceAsync;
import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistsTreePanel;
import org.cotrix.web.permissionmanager.client.codelists.tree.CodelistsTreePanel.CodelistsTreePanelListener;
import org.cotrix.web.permissionmanager.client.codelists.user.UserAddPanel;
import org.cotrix.web.permissionmanager.client.codelists.user.UserAddPanel.UserAddPanelListener;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix;
import org.cotrix.web.permissionmanager.client.matrix.UsersRolesMatrix.UsersRolesMatrixListener;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.CodelistVersion;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.share.client.error.ManagedFailureCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsPermissionsPanel extends ResizeComposite {



	private static CodelistsPermissionsPanelUiBinder uiBinder = GWT
			.create(CodelistsPermissionsPanelUiBinder.class);

	interface CodelistsPermissionsPanelUiBinder extends
	UiBinder<Widget, CodelistsPermissionsPanel> {
	}

	protected PermissionServiceAsync service = GWT.create(PermissionService.class);

	@UiField DeckLayoutPanel centralPanel;
	@UiField HTMLPanel blankPanel;
	@UiField DockLayoutPanel rolesPanel;
	@UiField(provided=true) UsersRolesMatrix usersRolesMatrix;
	@UiField(provided=true) CodelistsTreePanel codelistsTreePanel;
	@UiField(provided=true) UserAddPanel userAddPanel;

	protected String currentCodelistId = null;
	protected CodelistRolesRowDataProvider dataProvider = new CodelistRolesRowDataProvider(currentCodelistId);

	public CodelistsPermissionsPanel() {
		setupMatrix();
		setupTree();
		setupUserAddPanel();
		initWidget(uiBinder.createAndBindUi(this));
		centralPanel.showWidget(blankPanel);
	}

	protected void setupTree() {
		codelistsTreePanel = new CodelistsTreePanel(new CodelistsTreePanelListener() {

			@Override
			public void onCodelistSelected(CodelistVersion codelist) {
				Log.trace("onCodelistSelected "+codelist);
				showMatrix(codelist);
			}
		});
	}

	protected void setupMatrix() {
		usersRolesMatrix = new UsersRolesMatrix(new UsersRolesMatrixListener() {

			@Override
			public void onRolesRowUpdated(RolesRow row) {
				saveRow(row);
			}
		});
		service.getRoles(RolesType.CODELISTS, new ManagedFailureCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> result) {
				usersRolesMatrix.setupMatrix(result, dataProvider);
			}
		});
	}

	protected void setupUserAddPanel() {
		userAddPanel = new UserAddPanel(new UserAddPanelListener() {

			@Override
			public void onUserAdded(UIUser user) {
				Log.trace("onUserAdded "+user);
				RolesRow row = new RolesRow(user, new ArrayList<String>());
				dataProvider.getCache().add(row);
				dataProvider.refresh();
				saveRow(row);
			}
		});
	}

	protected void showMatrix(CodelistVersion codelist) {
		centralPanel.showWidget(rolesPanel);

		currentCodelistId = codelist.getId();
		dataProvider.setCodelistId(currentCodelistId);
		usersRolesMatrix.reload(codelist.getRoles());
	}

	protected void saveRow(RolesRow row) {
		service.codelistRolesRowUpdated(currentCodelistId, row, new ManagedFailureCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub

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
