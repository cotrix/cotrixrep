package org.cotrix.web.share.shared.json;

import java.util.ArrayList;

import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class HeaderTypeJson {
	private static String KEY_NAME = "NAME";
	private static String KEY_VALUE = "VALUE";
	private static String KEY_RELATED_VALUE = "RELATED_VALUE";

	public static ArrayList<HeaderType> formJSON(JSONArray json) {
		ArrayList<HeaderType> types = new ArrayList<HeaderType>();
		for (int i = 0; i < json.size(); i++) {
			JSONObject j = (JSONObject) json.get(i);
			HeaderType type = new HeaderType();
			if (j.get(KEY_NAME) != null) {
				JSONString str =  (JSONString) j.get(KEY_NAME);
				type.setName(str.stringValue());
			} else if (j.get(KEY_VALUE) != null) {
				JSONString str =  (JSONString) j.get(KEY_VALUE);
				type.setName(str.stringValue());
			} else if (j.get(KEY_RELATED_VALUE) != null) {
				JSONString str =  (JSONString) j.get(KEY_RELATED_VALUE);
				type.setName(str.stringValue());
			}
			types.add(type);
		}
		return types;
	}

	public static JSONArray toJSON(ArrayList<HeaderType> types) {
		JSONArray json = new JSONArray();
		for (int i = 0; i < types.size(); i++) {
			HeaderType type = types.get(i);
			JSONObject j = new JSONObject();
			if (type.getName() != null) {
				j.put(KEY_NAME, new JSONString(type.getName()));
			} else if (type.getValue() != null) {
				j.put(KEY_VALUE, new JSONString(type.getValue()));
			} else if (type.getRelatedValue() != null) {
				j.put(KEY_RELATED_VALUE, new JSONString(type.getRelatedValue()));
			}
			json.set(i, j);
		}
		return json;
	}

}
