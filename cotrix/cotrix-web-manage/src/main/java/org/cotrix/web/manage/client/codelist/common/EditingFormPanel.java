/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EditingFormPanel extends Composite {

	private static final int LABEL_COLUMN = 0;
	private static final int VALUE_COLUMN = 1;

	private FlexTable table;
	private CellFormatter cellFormatter;

	private String labelStyleName;
	private String valueStyleName;

	public EditingFormPanel() {
		table = new FlexTable();
		cellFormatter = table.getCellFormatter();
		initWidget(table);
	}

	public FormRow addRow(String label, Widget widget) {
		
		int row = table.getRowCount();

		Label labelWidget = new Label(label);
		table.setWidget(row, LABEL_COLUMN, labelWidget);
		cellFormatter.setStyleName(row, LABEL_COLUMN, labelStyleName);

		table.setWidget(row, VALUE_COLUMN, widget);
		cellFormatter.setStyleName(row, VALUE_COLUMN, valueStyleName);

		return new FormRow(labelWidget);
	}
	

	private int getLabelRow(Widget label) {

		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getCellCount(row); col++) {
				if (label == table.getWidget(row, col)) return row;
			}
		}
		return -1;
	}

	public class FormRow {

		private Widget label;

		/**
		 * @param label
		 */
		private FormRow(Widget label) {
			this.label = label;
		}
		
		


	}
}
