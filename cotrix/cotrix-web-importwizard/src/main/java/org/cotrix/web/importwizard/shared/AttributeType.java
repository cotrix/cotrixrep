/**
 * 
 */
package org.cotrix.web.importwizard.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum AttributeType {
	
	CODE("Code"),
	DESCRIPTION("Description");
	
	private String label;

	/**
	 * @param label
	 */
	private AttributeType(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}
