/**
 * 
 */
package org.cotrix.web.publish.shared;

import org.cotrix.web.publish.shared.AttributeMapping.Mapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum SdmxElement implements Mapping {
	FINAL("final"), 
	AGENCY("agency"), 
	VALID_FROM("validFrom"), 
	VALID_TO("validTo"), 
	NAME("name"), 
	DESCRIPTION("description"), 
	ANNOTATION("annotation"), 
	URI("uri");
	
	protected String label;

	/**
	 * @param label
	 */
	private SdmxElement(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}