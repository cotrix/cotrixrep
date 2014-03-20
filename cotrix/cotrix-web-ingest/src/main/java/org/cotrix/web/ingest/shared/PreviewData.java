/**
 * 
 */
package org.cotrix.web.ingest.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewData implements IsSerializable {
	
	protected List<String> headersLabels;
	protected boolean headersEditable;
	protected List<List<String>> rows;
	
	public PreviewData(){}
	
	/**
	 * @param headersLabels
	 * @param headersEditable
	 * @param rows
	 */
	public PreviewData(List<String> headersLabels,
			boolean headersEditable, List<List<String>> rows) {
		this.headersLabels = headersLabels;
		this.headersEditable = headersEditable;
		this.rows = rows;
	}
	/**
	 * @return the headersLabels
	 */
	public List<String> getHeadersLabels() {
		return headersLabels;
	}
	/**
	 * @return the headersEditable
	 */
	public boolean isHeadersEditable() {
		return headersEditable;
	}
	/**
	 * @return the rows
	 */
	public List<List<String>> getRows() {
		return rows;
	}

}