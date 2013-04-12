package org.cotrix.web.share.shared.json;

import java.util.ArrayList;

import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class HeaderTypeJson {
	public static String KEY_NAME = "NAME";
	public static String KEY_VALUE = "VALUE";
	public static String KEY_RELATED_VALUE = "RELATED_VALUE";

	public static JSONArray toJSON(ArrayList<HeaderType> types) {
		JSONArray json = new JSONArray();
		for (int i = 0; i < types.size(); i++) {
			HeaderType type = types.get(i);
			JSONObject j = new JSONObject();
			if (type.getName() != null) {
				j.put(KEY_NAME, new JSONString(type.getName()));
			} 
			if (type.getValue() != null) {
				j.put(KEY_VALUE, new JSONString(type.getValue()));
			} 
			if (type.getRelatedValue() != null && type.getRelatedValue().length() > 0) {
				j.put(KEY_RELATED_VALUE, new JSONString(type.getRelatedValue()));
			}
			json.set(i, j);
		}
		
		return json;
	}

}
