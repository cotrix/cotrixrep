/**
 * 
 */
package org.cotrix.web.ingest.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportResult implements IsSerializable {
	
	private boolean mappingFailed;
	private String codelistId;
	
	public ImportResult(){}
	

	public ImportResult(String codelistId) {
		this.codelistId = codelistId;
		this.mappingFailed = false;
	}

	public ImportResult(boolean mappingFailed) {
		this.mappingFailed = mappingFailed;
	}

	public boolean isMappingFailed() {
		return mappingFailed;
	}

	public void setMappingFailed(boolean mappingFailed) {
		this.mappingFailed = mappingFailed;
	}

	public String getCodelistId() {
		return codelistId;
	}

	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImportResult [mappingFailed=");
		builder.append(mappingFailed);
		builder.append(", codelistId=");
		builder.append(codelistId);
		builder.append("]");
		return builder.toString();
	}
}
