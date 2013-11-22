/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.matrix;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

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
	
	protected static List<String> ROLES = Arrays.asList("USER", "EDITOR", "REVIEWER", "PUBLISHER");
	protected static List<RolesRow> ROWS = Arrays.asList(
			new RolesRow(new User("1", "Federico De Faveri"), ROLES.subList(0, 2)),
			new RolesRow(new User("2", "Fabio Simeoni"), ROLES.subList(0, 2)),
			new RolesRow(new User("3", "Anton Ellenbroek"), ROLES.subList(2, 3)),
			new RolesRow(new User("4", "Aureliano Gentile"), ROLES.subList(1, 2)),
			new RolesRow(new User("5", "Erik Van Ingen"), ROLES.subList(2, 3))			
			);
	
	@UiField DataGrid<RolesRow> matrix;

	public UsersRolesMatrix() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	protected DataGrid<RolesRow> setupMatrix() {
		DataGrid<RolesRow> matrix = new DataGrid<RolesRow>();
		
		Column<RolesRow, String> userColumn = new Column<RolesRow, String>(new TextCell()) {
			
			@Override
			public String getValue(RolesRow row) {
				return row.getUser().getUsername();
			}
		};
		matrix.addColumn(userColumn, "Users");
		
		for (String role:ROLES) {
			Column<RolesRow, Boolean> roleColumns = getColumn(role);
			matrix.addColumn(roleColumns, role);
		}
		
		ListDataProvider<RolesRow> dataProvider = new ListDataProvider<RolesRow>(ROWS);
		dataProvider.addDataDisplay(matrix);
		
		return matrix;
	}
	
	protected Column<RolesRow, Boolean> getColumn(final String role) {
		Column<RolesRow, Boolean> column = new Column<RolesRow, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(RolesRow row) {
				return row.hasRole(role);
			}
		};
		
		column.setFieldUpdater(new FieldUpdater<RolesRow, Boolean>() {
			
			@Override
			public void update(int index, RolesRow row, Boolean value) {
				row.addRole(role);
			}
		});
		
		return column;
	}

}
