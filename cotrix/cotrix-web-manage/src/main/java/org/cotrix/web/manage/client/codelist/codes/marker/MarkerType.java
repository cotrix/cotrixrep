/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import static org.cotrix.web.manage.client.codelist.codes.marker.MarkersResource.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * blue #d5eaff #2a4f74
 * red #ffdede #771e1e
 * green #d6ffd5 #2e5a2d
 */
public enum MarkerType {

	
	DELETED("DELETED", style.deletedBackgroundColor(), style.deletedTextColor(), style.deletedHighlight(), false, false, "deleted"),
	INVALID("INVALID", style.invalidBackgroundColor(), style.invalidTextColor(), style.invalidHighlight(), true, true, "invalid"),
	NEWCODE("NEW", style.newCodeBackgroundColor(), style.newCodeTextColor(), style.newCodeHighlight(), true, true, "new"),
	MODIFIED("MODIFIED", style.modifiedBackgroundColor(), style.modifiedTextColor(), style.modifiedHighlight(), true, true, "modified");
	
	private String name;
	private String backgroundColor;
	private String textColor;
	private String highlightStyleName;
	private boolean readOnly;
	private boolean descriptionReadOnly;
	private String definitionName;

	private MarkerType(String name, String backgroundColor, String textColor, String highlightStyleName,
			boolean readOnly, boolean descriptionReadOnly, String definitionName) {
		this.name = name;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.highlightStyleName = highlightStyleName;
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
	
	public String getHighlightStyleName() {
		return highlightStyleName;
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
