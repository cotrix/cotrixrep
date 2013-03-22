package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Codelist implements IsSerializable{
	String name;
	int id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
