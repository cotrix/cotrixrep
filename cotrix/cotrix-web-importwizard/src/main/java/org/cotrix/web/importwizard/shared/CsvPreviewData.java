/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewData implements Serializable {
	
	private static final long serialVersionUID = 1123760247528878537L;
	
	protected List<String> header;
	protected int columnsCount;
	protected List<List<String>> data;
	
	public CsvPreviewData(){}

	/**
	 * @param header
	 * @param columnsCount
	 * @param data
	 */
	public CsvPreviewData(List<String> header, int columnsCount,
			List<List<String>> data) {
		this.header = header;
		this.columnsCount = columnsCount;
		this.data = data;
	}

	/**
	 * @return the header
	 */
	public List<String> getHeader() {
		return header;
	}

	/**
	 * @return the columnsCount
	 */
	public int getColumnsCount() {
		return columnsCount;
	}

	/**
	 * @return the data
	 */
	public List<List<String>> getData() {
		return data;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(List<String> header) {
		this.header = header;
	}

	/**
	 * @param columnsCount the columnsCount to set
	 */
	public void setColumnsCount(int columnsCount) {
		this.columnsCount = columnsCount;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<List<String>> data) {
		this.data = data;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeListPreviewData [header=");
		builder.append(header);
		builder.append(", columnsCount=");
		builder.append(columnsCount);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}
