package org.cotrix.domain.utils;

import javax.xml.namespace.QName;


public class Utils {
	
	public static void notNull(String name, Object o) throws IllegalArgumentException {
		if (o==null)
			throw new IllegalArgumentException(name+" is null");
	}
	
	public static void notEmpty(String name, String o) throws IllegalArgumentException {
		if (o.isEmpty())
			throw new IllegalArgumentException(name+" is empty");
	}
	
	public static void notNullorEmpty(String name, String o) throws IllegalArgumentException {
		notNull(name, o);
		notEmpty(name,o);
	}
	
	public static void valid(QName name) throws IllegalArgumentException {
		notNull("name", name);
		notNullorEmpty("name",name.getLocalPart());
	}

}
