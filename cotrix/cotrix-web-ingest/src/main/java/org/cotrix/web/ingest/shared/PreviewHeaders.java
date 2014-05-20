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
public class PreviewHeaders implements IsSerializable {
	
	private boolean editable;
	private List<String> labels;
	
	public PreviewHeaders(){}
	
	/**
	 * @param editable
	 * @param labels
	 */
	public PreviewHeaders(boolean editable, List<String> labels) {
		this.editable = editable;
		this.labels = labels;
	}
	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * @return the headers
	 */
	public List<String> getLabels() {
		return labels;
	}

}
