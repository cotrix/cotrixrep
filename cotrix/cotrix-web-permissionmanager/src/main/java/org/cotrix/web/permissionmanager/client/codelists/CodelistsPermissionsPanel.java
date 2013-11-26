/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists;

import java.util.ArrayList;
import java.util.List;

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
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.UserLoggedEvent;

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

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsPermissionsPanel extends ResizeComposite {

	interface CodelistsPermissionsPanelUiBinder extends
	UiBinder<Widget, CodelistsPermissionsPanel> {
	}

	@Inject
	protected PermissionServiceAsync service;
	
	@Inject
	@CotrixBus
	protected EventBus cotrixBus;

	@UiField DeckLayoutPanel centralPanel;
	@UiField HTMLPanel blankPanel;
	@UiField DockLayoutPanel rolesPanel;
	@Inject @UiField(provided=true) UsersRolesMatrix usersRolesMatrix;
	@Inject @UiField(provided=true) CodelistsTreePanel codelistsTreePanel;
	@Inject @UiField(provided=true) UserAddPanel userAddPanel;

	protected String currentCodelistId = null;
	protected CodelistRolesRowDataProvider dataProvider = new CodelistRolesRowDataProvider(currentCodelistId);

	@Inject
	protected void init(CodelistsPermissionsPanelUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		centralPanel.showWidget(blankPanel);
	}
	
	@Inject
	protected void bind() {
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
	protected void setupTree() {
		codelistsTreePanel.setListener(new CodelistsTreePanelListener() {

			@Override
			public void onCodelistSelected(CodelistVersion codelist) {
				Log.trace("onCodelistSelected "+codelist);
				showMatrix(codelist);
			}
		});
	}

	@Inject
	protected void setupMatrix() {
		usersRolesMatrix.setListener(new UsersRolesMatrixListener() {

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

	@Inject
	protected void setupUserAddPanel() {
		userAddPanel.setListener(new UserAddPanelListener() {

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
		Log.trace("showMatrix "+codelist);
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
