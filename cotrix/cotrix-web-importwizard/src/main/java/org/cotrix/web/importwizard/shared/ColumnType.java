/**
 * 
 */
package org.cotrix.web.importwizard.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum ColumnType {
	
	CODE("Code"),
	DESCRIPTION("Description");
	
	private String label;

	/**
	 * @param label
	 */
	private ColumnType(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}
