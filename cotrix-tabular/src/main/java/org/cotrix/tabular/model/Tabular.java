package org.cotrix.tabular.model;

import java.util.List;

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
