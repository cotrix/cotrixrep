package org.cotrix;

import javax.xml.namespace.QName;

public class Fixture {

	public static final String ns = "http://acme/org";

	public static QName name = new QName(ns,"name1");
	public static QName name2 = new QName(ns,"name2");
	public static QName name3 = new QName(ns,"name3");
	
	public static QName type = new QName(ns,"type1");
	public static QName type2 = new QName(ns,"type2");
	public static QName type3 = new QName(ns,"type3");
	
	public static String value = "value";
	public static String value2 = "value2";
	
	public static String language = "lang"; 

}
