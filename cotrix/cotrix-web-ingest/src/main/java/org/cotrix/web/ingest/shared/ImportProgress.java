/**
 * 
 */
package org.cotrix.web.ingest.shared;

import org.cotrix.web.common.shared.Progress;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgress extends Progress {
	
	protected boolean mappingFailed;
	
	/**
	 * @return the mappingFailed
	 */
	public boolean isMappingFailed() {
		return mappingFailed;
	}

	/**
	 * @param mappingFailed the mappingFailed to set
	 */
	public void setMappingFailed() {
		setDone();
		this.mappingFailed = true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImportProgress [mappingFailed=");
		builder.append(mappingFailed);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
