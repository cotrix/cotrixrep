/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public enum MarkerType {
	DELETED("DELETED", "#d5eaff", "#2a4f74", false, false, "deleted"),
	INVALID("INVALID", "#ffdede", "#771e1e", true, true, "invalid"),
	ANOTHER_MARKER("ANOTHER MARKER", "#d6ffd5", "#2e5a2d", false, true, "another_marker")
	
	;
	
	private String name;
	private String backgroundColor;
	private String textColor;
	private boolean readOnly;
	private boolean descriptionReadOnly;
	private String definitionName;

	private MarkerType(String name, String backgroundColor, String textColor,
			boolean readOnly, boolean descriptionReadOnly, String definitionName) {
		this.name = name;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.readOnly = readOnly;
		this.descriptionReadOnly = descriptionReadOnly;
		this.definitionName = definitionName;
	}

	public String getName() {
		return name;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public String getTextColor() {
		return textColor;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isDescriptionReadOnly() {
		return descriptionReadOnly;
	}

	public String getDefinitionName() {
		return definitionName;
	}
	
	public static MarkerType fromDefinitionName(String definitionName) {
		for (MarkerType type:values()) if (type.getDefinitionName().equals(definitionName)) return type;
		return null;
	}
}
