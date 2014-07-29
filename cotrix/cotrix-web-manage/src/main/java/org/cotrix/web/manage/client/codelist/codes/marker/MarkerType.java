/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public enum MarkerType {
	
	DELETED("DELETED", false, false, "deleted", MarkerEventExtractor.NONE),
	INVALID("INVALID",  true, true, "invalid", MarkerEventExtractor.PARSE),
	NEWCODE("NEW", true, false, "new", MarkerEventExtractor.NONE),
	MODIFIED("MODIFIED", true, true, "modified", MarkerEventExtractor.PARSE);
	
	private String name;
	private boolean readOnly;
	private boolean descriptionReadOnly;
	private String definitionName;
	private MarkerEventExtractor eventExtractor;

	private MarkerType(String name, 
			boolean readOnly, boolean descriptionReadOnly, String definitionName, MarkerEventExtractor eventExtractor) {
		this.name = name;
		this.readOnly = readOnly;
		this.descriptionReadOnly = descriptionReadOnly;
		this.definitionName = definitionName;
		this.eventExtractor = eventExtractor;
	}

	public String getName() {
		return name;
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
	
	public MarkerEventExtractor getEventExtractor() {
		return eventExtractor;
	}

	public static MarkerType fromDefinitionName(String definitionName) {
		for (MarkerType type:values()) if (type.getDefinitionName().equals(definitionName)) return type;
		return null;
	}
}
