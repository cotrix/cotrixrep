package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UICode implements IsSerializable{
	String id;
	String name;
	UIAttribute attribute;
	UICodelist parent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UIAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(UIAttribute attribute) {
		this.attribute = attribute;
	}
	public UICodelist getParent() {
		return parent;
	}
	public void setParent(UICodelist parent) {
		this.parent = parent;
	}
}
