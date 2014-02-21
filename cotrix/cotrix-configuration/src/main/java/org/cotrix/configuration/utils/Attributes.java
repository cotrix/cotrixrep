package org.cotrix.configuration.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlType
public class Attributes {

	@XmlAnyAttribute
	Map<QName,String> attributes = new HashMap<QName,String>();
	
	public Map<String,String> asMap() {
		Map<String,String> properties = new HashMap<String, String>();
		for (Map.Entry<QName,String> e : attributes.entrySet())
			properties.put(e.getKey().getLocalPart(),e.getValue());
		return properties;
	}
	
	public Properties asProperties() {
		Properties ps = new Properties();
		ps.putAll(asMap());
		return ps;
	}
}
