package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UIChanelProperty implements IsSerializable{
	private String name;
	private String description;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}