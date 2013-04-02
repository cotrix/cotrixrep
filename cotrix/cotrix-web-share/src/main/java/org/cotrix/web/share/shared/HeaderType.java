package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HeaderType implements IsSerializable {
	private String name;
	private String value;
	private String relatedValue;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
