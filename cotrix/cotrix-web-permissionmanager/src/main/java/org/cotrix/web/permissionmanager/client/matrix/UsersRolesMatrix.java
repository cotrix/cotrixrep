/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.widgets.LoadingPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UsersRolesMatrix extends ResizeComposite {

	private static UsersRolesMatrixUiBinder uiBinder = GWT
			.create(UsersRolesMatrixUiBinder.class);

	interface UsersRolesMatrixUiBinder extends
	UiBinder<Widget, UsersRolesMatrix> {
	}

	public interface UsersRolesMatrixListener {
		public void onRolesRowUpdated(RolesRow row);
	}

	@UiField LoadingPanel loader;
	@UiField(provided=true) DataGrid<RolesRow> matrix;

	protected UsersRolesMatrixListener listener;
	protected List<String> userRoles = new ArrayList<String>();

	public UsersRolesMatrix() {
		matrix = new DataGrid<RolesRow>();
		initWidget(uiBinder.createAndBindUi(this));
		loader.showLoader();
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(UsersRolesMatrixListener listener) {
		this.listener = listener;
	}

	public void reload(List<String> userRoles) {
		Log.trace("reload");
		this.userRoles.clear();
		this.userRoles.addAll(userRoles);
		int pageSize = matrix.getPageSize();
		matrix.setVisibleRangeAndClearData(new Range(0, pageSize), true);
	}

	public void setupMatrix(List<String> roles, AbstractDataProvider<RolesRow> dataProvider) {

		Column<RolesRow, String> userColumn = new Column<RolesRow, String>(new TextCell()) {

			@Override
			public String getValue(RolesRow row) {
				return row.getUser().getUsername();
			}
		};
		matrix.addColumn(userColumn, "Users");

		for (String role:roles) {
			Column<RolesRow, RoleState> roleColumns = getColumn(role);
			matrix.addColumn(roleColumns, role);
		}

		dataProvider.addDataDisplay(matrix);

		loader.hideLoader();
	}

	protected Column<RolesRow, RoleState> getColumn(final String role) {
		Column<RolesRow, RoleState> column = new Column<RolesRow, RoleState>(new RoleCell()) {

			@Override
			public RoleState getValue(RolesRow row) {
				RoleState roleState = new RoleState(userRoles.contains(role), row.hasRole(role));
				return roleState;
			}
		};

		column.setFieldUpdater(new FieldUpdater<RolesRow, RoleState>() {

			@Override
			public void update(int index, RolesRow row, RoleState value) {
				row.addRole(role);
				listener.onRolesRowUpdated(row);
			}
		});

		return column;
	}

}
