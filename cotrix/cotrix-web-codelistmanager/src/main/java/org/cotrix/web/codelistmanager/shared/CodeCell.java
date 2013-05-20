package org.cotrix.web.codelistmanager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CodeCell  implements IsSerializable{
	String id;
	String value;
	String codelistId;
	String codelistName;
	String language; 
	String name;
	String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCodelistId() {
		return codelistId;
	}
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}
	public String getCodelistName() {
		return codelistName;
	}
	public void setCodelistName(String codelistName) {
		this.codelistName = codelistName;
	}
}