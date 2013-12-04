/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import java.util.List;

import org.cotrix.web.permissionmanager.client.PermissionBus;
import org.cotrix.web.permissionmanager.client.matrix.user.UserAddedEvent;
import org.cotrix.web.permissionmanager.client.matrix.user.UserSuggestOracle;
import org.cotrix.web.permissionmanager.client.matrix.user.UserSuggestion;
import org.cotrix.web.permissionmanager.client.resources.PermissionsResources;
import org.cotrix.web.permissionmanager.shared.RoleState;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.share.client.resources.CotrixSimplePager;
import org.cotrix.web.share.client.widgets.LoadingPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.Range;
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

		String groupHeaderCell();
		
		String textCell();

		String language();
		
		String closeGroup();
		
		String emptyTableWidget();
		
		String currentUser();
		
		String otherUser();
	}

	@UiField LoadingPanel loader;
	@UiField(provided=true) DataGrid<RolesRow> matrix;
	
	@UiField(provided = true)
	SimplePager pager;
	
	@Inject @PermissionBus
	protected EventBus bus;
	
	@Inject
	protected UserSuggestOracle oracle;

	protected DataGridResources dataGridResources = GWT.create(DataGridResources.class);

	@Inject
	protected void init(UsersRolesMatrixUiBinder uiBinder) {
		matrix = new DataGrid<RolesRow>(20, dataGridResources);
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

	public void setupMatrix(List<String> roles, AbstractDataProvider<RolesRow> dataProvider) {
		
		UserCell userCell = new UserCell(oracle) {

			/** 
			 * {@inheritDoc}
			 */
			@Override
			public void onSuggestBoxCreated(final SuggestBox suggestBox) {
				suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						Log.trace("selected suggestion "+event.getSelectedItem());
						if (event.getSelectedItem() instanceof UserSuggestion) {
							UserSuggestion userSuggestion = (UserSuggestion)event.getSelectedItem();
							bus.fireEvent(new UserAddedEvent(userSuggestion.getUser()));
						}
						suggestBox.setText("");
					}
				});
			}
			
		};
		
		Column<RolesRow, String> userColumn = new UserColumn(userCell);
		
		ImageResourceCell headerCell = new ImageResourceCell();
		Header<ImageResource> header = new Header<ImageResource>(headerCell) {
			
			@Override
			public ImageResource getValue() {
				return PermissionsResources.INSTANCE.users();
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
				if (!(row instanceof EditorRow)) {
					row.setLoading(true);
					matrix.redrawRow(index);
					bus.fireEventFromSource(new RolesRowUpdatedEvent(row, role, value.isChecked()), UsersRolesMatrix.this);
				}
			}
		});

		return column;
	}

}
