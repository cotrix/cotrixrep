package org.cotrix.web.importwizard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HeaderType implements IsSerializable {
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
