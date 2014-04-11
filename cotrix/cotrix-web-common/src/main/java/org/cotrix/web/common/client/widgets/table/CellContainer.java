package org.cotrix.web.common.client.widgets.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CellContainer extends AbstractRow implements IsWidget {
	
	private List<Widget> cells;
	private String firstCellStyle;
	private String secondCellStyle;
	
	public CellContainer() {
		cells = new ArrayList<Widget>();
	}
	
	public String getFirstCellStyle() {
		return firstCellStyle;
	}

	public void setFirstCellStyle(String firstCellStyle) {
		this.firstCellStyle = firstCellStyle;
	}

	public String getSecondCellStyle() {
		return secondCellStyle;
	}

	public void setSecondCellStyle(String secondCellStyle) {
		this.secondCellStyle = secondCellStyle;
	}
	
	@Override
	public void setup() {
		int column = 0;
		for (Widget cell:cells) {
			table.setWidget(rowIndex, column, cell);
			if (column == 0 && firstCellStyle!=null) table.getCellFormatter().setStyleName(rowIndex, column, firstCellStyle);
			if (column == 1 && secondCellStyle!=null) table.getCellFormatter().setStyleName(rowIndex, column, secondCellStyle);
			column++;
		}
		
		table.getFlexCellFormatter().setColSpan(rowIndex, 1, 3);
	}

	/**
	 * @return the cells
	 */
	public List<Widget> getCells() {
		return cells;
	}

	@UiChild(tagname="cell", limit=2)
	public void addCell(Widget widget) {
		cells.add(widget);
	}

	@Override
	public Widget asWidget() {
		return null;
	}
}