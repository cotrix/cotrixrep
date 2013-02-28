package org.cotrix.web.importwizard.shared;

public class HeaderType {
	private String value;
	private String relatedValue;
	
	public boolean hasRelatedValue(){
		return (relatedValue == null)?false:true;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRelatedValue() {
		return relatedValue;
	}
	public void setRelatedValue(String relatedValue) {
		this.relatedValue = relatedValue;
	}
}
