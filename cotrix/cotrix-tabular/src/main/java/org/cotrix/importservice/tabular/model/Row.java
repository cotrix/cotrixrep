package org.cotrix.importservice.tabular.model;

import java.util.Map;

/**
 * A row of a table.
 * 
 * @author Fabio Simeoni
 *
 */
public class Row {

	Map<String,String> data;
	
	/**
	 * Creates an instance with the named values of the row.
	 * @param data
	 */
	public Row(Map<String,String> data) {
		this.data=data;
	}
	
	/**
	 * Returns the value of this row for a given column.
	 * @param column the column name
	 * @return the value
	 */
	public String get(String column) {
		return data.get(column);
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}
