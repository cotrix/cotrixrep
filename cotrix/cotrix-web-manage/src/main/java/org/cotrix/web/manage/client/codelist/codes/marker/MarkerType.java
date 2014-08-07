/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public enum MarkerType {
	
	DELETED("DELETED", "will not be part of future versions.", false, false, "deleted", MarkerEventExtractor.NONE),
	INVALID("INVALID", "does not satisfy all schema constraints.", true, true, "invalid", MarkerEventExtractor.PARSE),
	NEWCODE("NEW", "First added in this version.", true, false, "new", MarkerEventExtractor.NONE),
	MODIFIED("MODIFIED", "has changed in this version.", true, true, "modified", MarkerEventExtractor.PARSE);
	
	private String name;
	private String description;
	private boolean readOnly;
	private boolean descriptionReadOnly;
	private String definitionName;
	private MarkerEventExtractor eventExtractor;

	private MarkerType(String name, String description, 
			boolean readOnly, boolean descriptionReadOnly, String definitionName, MarkerEventExtractor eventExtractor) {
		this.name = name;
		this.description = description;
		this.readOnly = readOnly;
		this.descriptionReadOnly = descriptionReadOnly;
		this.definitionName = definitionName;
		this.eventExtractor = eventExtractor;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
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
