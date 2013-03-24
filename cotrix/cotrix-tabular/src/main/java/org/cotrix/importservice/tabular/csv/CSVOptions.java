package org.cotrix.importservice.tabular.csv;

import java.util.List;

/**
 * Parsing options for CSV data.
 * 
 * @author Fabio Simeoni
 *
 */
public class CSVOptions {

	public static final char defaultDelimiter=',';
	
	private char delimiter = defaultDelimiter;
	
	private List<String> columns;
	
	/**
	 * Return the delimiter character used in the data, {@link #defaultDelimiter} by default.
	 * @return the delimenter character
	 */
	public char delimiter() {
		return delimiter;
	}
	
	/**
	 * Sets the delimeter character used in the data, overriding the {@link #defaultDelimiter}.
	 * @param delimiter the delimiter character.
	 */
	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
	
	/**
	 * Returns the names of the columns in the data.
	 * @return the columns
	 */
	public List<String> columns() {
		return columns;
	}
	
	/**
	 * Sets the names of the columns in the data.
	 * @param columns the columns
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
