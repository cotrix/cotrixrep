/**
 * 
 */
package org.cotrix.web.common.client.widgets.table;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Table extends Composite {
	
	private FlexTable table;
	
	public Table() {
		table = new FlexTable();
		initWidget(table);
	}
	
	@UiChild(tagname="row")
	public void addRow(Row row) {
		row.setTable(table, table.getRowCount());
	}
	
	public void insertRow(int beforeRow, Row row) {
		table.insertRow(beforeRow);
		row.setTable(table, beforeRow);
	}
	
	public interface Row {
		public void setTable(FlexTable table, int rowIndex);
	}
	
	public FlexTable getFlexTable() {
		return table;
	}
}
