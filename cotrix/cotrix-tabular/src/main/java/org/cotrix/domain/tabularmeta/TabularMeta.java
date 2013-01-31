package org.cotrix.domain.tabularmeta;

import java.util.List;

/**
 * TabularMeta represents the metadata of data in a tabular format. It has only metadata, so it does not even have the
 * headernames, these are in Tabular. It refers to the headers of the tabular data by the order of the headers.
 * 
 * 
 * 
 * @see org.cotrix.domain.tabular.Tabular on how to represent the real tabular data.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularMeta {

	private List<Header> headerList;

	public List<Header> getHeaderList() {
		return headerList;
	}

	/**
	 * the order of the Headers corresponds with the order of the columns in the tabular data.
	 * 
	 * @param headerList
	 */
	public void setHeaderList(List<Header> headerList) {
		this.headerList = headerList;
	}

}
