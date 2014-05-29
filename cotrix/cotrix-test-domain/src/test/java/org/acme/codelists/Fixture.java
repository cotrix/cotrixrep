package org.acme.codelists;

import javax.xml.namespace.QName;

public class Fixture {

	public static final String ns = "http://acme/org";

	public static QName name = new QName(ns,"name1");
	public static QName name2 = new QName(ns,"name2");
	public static QName name3 = new QName(ns,"name3");
	public static QName newname = name3; //alias for test legibility
	
	public static QName type = new QName(ns,"type");
	public static QName type2 = new QName(ns,"type2");
	public static QName type3 = new QName(ns,"type3"); //alias for test legibility
	public static QName newtype = type3;
	
	public static String value = "value";
	public static String value2 = "value2";
	public static String value3 = "value3";
	public static String newvalue = value3; //alias for test legibility
	
	public static String language = "lang"; 
	public static String language2 = "lang2"; 
	public static String newlanguage = language2; //alias for test legibility
	
	public static String description = "desc"; 
	public static String description2 = "desc2"; 
	public static String newdescription = description2; //alias for test legibility
	
	public static String version = "0.1";
	public static String version2 = "0.2";
	public static String version3 = "0.3";
	public static String newversion = version3;//alias for test legibility
	
}
