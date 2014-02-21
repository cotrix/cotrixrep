/**
 * 
 */
package org.cotrix.web.users.client.matrix;

import java.util.List;

import org.cotrix.web.common.client.resources.CotrixSimplePager;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.users.client.PermissionBus;
import org.cotrix.web.users.client.resources.PermissionsResources;
import org.cotrix.web.users.shared.RoleState;
import org.cotrix.web.users.shared.RolesRow;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UsersRolesMatrix extends ResizeComposite {

	final static RoleState NO_ROLE = new RoleState(false, false, false);
	final static RoleState NO_ROLE_LOADING = new RoleState(false, false, true);

	interface UsersRolesMatrixUiBinder extends UiBinder<Widget, UsersRolesMatrix> {}

	interface DataGridResources extends DataGrid.Resources {

		@Source("Matrix.css")
		DataGridStyle dataGridStyle();
	}

	interface DataGridStyle extends DataGrid.Style {

		String emptyTableWidget();
	}

	@UiField LoadingPanel loader;
	@UiField(provided=true) DataGrid<RolesRow> matrix;

	@UiField(provided = true)
	SimplePager pager;

	@Inject @PermissionBus
	protected EventBus bus;

	protected SingleSelectionModel<RolesRow> selectionModel = new SingleSelectionModel<RolesRow>();

	@Inject
	protected void init(UsersRolesMatrixUiBinder uiBinder, DataGridResources dataGridResources) {
		matrix = new DataGrid<RolesRow>(20, dataGridResources);
		matrix.setSelectionModel(selectionModel);
		// Create a Pager to control the table.
		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(matrix);

		initWidget(uiBinder.createAndBindUi(this));
		loader.showLoader();
	}

	public void refresh() {
		int pageSize = matrix.getPageSize();
		matrix.setVisibleRangeAndClearData(new Range(0, pageSize), true);
	}

	public RolesRow getSelectedRow() {
		return selectionModel.getSelectedObject();
	}

	public void setupMatrix(List<String> roles, AbstractDataProvider<RolesRow> dataProvider) {

		Column<RolesRow, String> userColumn = new Column<RolesRow, String>(new TextCell()) {

			@Override
			public String getValue(RolesRow row) {
				return row.getUser()!=null?row.getUser().getFullName():null;
			}

		};

		ImageResourceCell headerCell = new ImageResourceCell();
		Header<ImageResource> header = new Header<ImageResource>(headerCell) {

			@Override
			public ImageResource getValue() {
				return PermissionsResources.INSTANCE.usersGrey();
			}
		};


		userColumn.setSortable(true);

		matrix.addColumn(userColumn, header);
		matrix.addColumnSortHandler(new AsyncHandler(matrix));

		for (String role:roles) {
			Column<RolesRow, RoleState> roleColumns = getColumn(role);
			matrix.addColumn(roleColumns, role.toUpperCase());
		}

		dataProvider.addDataDisplay(matrix);

		loader.hideLoader();
	}

	protected Column<RolesRow, RoleState> getColumn(final String role) {
		Column<RolesRow, RoleState> column = new Column<RolesRow, RoleState>(new RoleCell()) {

			@Override
			public RoleState getValue(RolesRow row) {
				RoleState state = row.getRoleState(role);
				return state!=null?state:row.isLoading()?NO_ROLE_LOADING:NO_ROLE;
			}
		};

		column.setFieldUpdater(new FieldUpdater<RolesRow, RoleState>() {

			@Override
			public void update(int index, RolesRow row, RoleState value) {
				row.setLoading(true);
				matrix.redrawRow(index);
				bus.fireEventFromSource(new RolesRowUpdatedEvent(index, row, role, value.isChecked()), UsersRolesMatrix.this);

			}
		});

		return column;
	}

	public void redrawRow(int index) {
		matrix.redrawRow(index);
	}
}
