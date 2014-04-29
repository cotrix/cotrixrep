/**
 * 
 */
package org.cotrix.web.common.client.widgets.table;

import org.cotrix.web.common.client.widgets.table.Table.Row;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractRow implements Row {

	protected FlexTable table;
	protected int rowIndex;
	
	@Override
	public void setTable(FlexTable table, int rowIndex) {
		this.table = table;
		this.rowIndex = rowIndex;
		setup();
	}
	
	public Position getCellPosition(Widget cell) {
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getCellCount(row); col++) {
				if (cell == table.getWidget(row, col)) return new Position(row, col);
			}
		}
		return null;
	
	}
	
	public abstract void setup();
	
	public void addCell(int column, Widget cell) {
		table.setWidget(rowIndex, column, cell);
	}
	
	public void setCellStyle(int column, String style) {
		table.getCellFormatter().setStyleName(rowIndex, column, style);
	}
	
	public void setVisible(boolean visible) {
		table.getRowFormatter().setVisible(rowIndex, visible);		
	}
	
	public boolean isVisible() {
		return table.getRowFormatter().isVisible(rowIndex);
	}
	
	public class Position {
		int row;
		int col;

		public Position(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
	}

}
