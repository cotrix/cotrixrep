package org.cotrix.tabular.model;

import java.util.List;

/**
 * Tabular or TabularData represents data in a tabular format. This can be used to represent a CSV or an Excell file.
 * 
 * @see org.cotrix.tabular.modelmeta.TabularMeta on how to represent the headers.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class Tabular {

	private List<String> header;

	private List<List<String>> rows;

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

}
