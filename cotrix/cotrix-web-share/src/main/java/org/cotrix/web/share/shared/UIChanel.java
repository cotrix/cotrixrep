package org.cotrix.web.share.shared;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UIChanel implements IsSerializable{
	private String name;
	private ArrayList<UIChanelAssetType> assetTypes;
	private ArrayList<UIChanelProperty> properties;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<UIChanelAssetType> getAssetTypes() {
		return assetTypes;
	}
	public void setAssetTypes(ArrayList<UIChanelAssetType> assetTypes) {
		this.assetTypes = assetTypes;
	}
	public ArrayList<UIChanelProperty> getProperties() {
		return properties;
	}
	public void setProperties(ArrayList<UIChanelProperty> properties) {
		this.properties = properties;
	}
}
