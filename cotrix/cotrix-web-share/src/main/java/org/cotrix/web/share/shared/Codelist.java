package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Codelist implements IsSerializable{
	String name;
	String id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
