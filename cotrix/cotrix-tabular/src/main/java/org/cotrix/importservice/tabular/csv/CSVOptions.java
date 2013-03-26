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
	public static final char defaultQuote='"';
	
	private char delimiter = defaultDelimiter;
	private char quote = defaultQuote;
	
	private List<String> columns;
	
	private int rows = Integer.MAX_VALUE;
	
	/**
	 * Return the delimiter character used in the data, {@link #defaultDelimiter} by default.
	 * @return the delimenter character
	 */
	public char delimiter() {
		return delimiter;
	}
	
	/**
	 * Return the quote character used in the data, {@link #defaultQuote} by default.
	 * @return the quote character
	 */
	public char quote() {
		return quote;
	}
	
	/**
	 * Sets the delimeter character used in the data, overriding the {@link #defaultDelimiter}.
	 * @param delimiter the delimiter character.
	 */
	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Sets the quote character used in the data, overriding the {@link #defaultQuote}.
	 * @param quote the quote character.
	 */
	public void setQuote(char quote) {
		this.quote = quote;
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
	
	/**
	 * Returns the number of rows to be processed, all by default.
	 * @return the number of rows to be processed
	 */
	public int rows() {
		return rows;
	}
	
	/**
	 * Sets the number of rows to be processed.
	 * 
	 * @param rows the number of rows to be processed
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
}
