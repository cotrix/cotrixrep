package org.acme;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Identified.Abstract;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.version.DefaultVersion;

public class Fixture {

	public static final String ns = "http://acme/org";

	public static QName name = new QName(ns,"name1");
	public static QName name2 = new QName(ns,"name2");
	public static QName name3 = new QName(ns,"name3");
	
	public static QName type = new QName(ns,"type");
	public static QName type2 = new QName(ns,"type2");
	public static QName type3 = new QName(ns,"type3");
	
	public static String value = "value";
	public static String value2 = "value2";
	public static String value3 = "value3";
	
	
	public static Attribute a = attr("id").name(name).value(value).ofType(type).build();
	public static Attribute a2 = attr("id2").name(name2).value(value2).ofType(type2).build();
	public static Attribute a3 = attr("id3").name(name3).value(value3).ofType(type3).build();
	
	public static Code c = code("id").name(name).attributes(a).build();
	public static Code c2 = code("id2").name(name2).attributes(a,a2).build();
	public static Code c3 = code("id3").name(name3).attributes(a,a2,a3).build();
	
	public static <T extends Abstract<T>> Container.Private<T> container(T ... ts) {
		return new Container.Private<T>(Arrays.asList(ts));
	}
	
	public static <T extends Named> Map<QName,List<T>> asMap(Container<T> container) {
		
		Map<QName,List<T>> objects = new HashMap<QName,List<T>>();
		
		for (T object : container) {
			List<T> list = objects.get(object.name());
			if (list==null) {
				list = new ArrayList<T>();
				objects.put(object.name(),list);
			}
			list.add(object);
		}
		return objects;
	}
	
	public static <T> List<T> asList(Container<T> container) {
		
		List<T> objects = new ArrayList<T>();
		
		for (T object : container) 
			objects.add(object);
		
		return objects;
	}
	
	public static String version = "1.0";
	
	public static DefaultVersion no_version = new DefaultVersion();
	
	public static Codelist cl = codelist("id1").name(name).with(c).attributes(a).version(no_version.value()).build();
	public static Codelist cl2 = codelist("id2").name(name2).with(c,c2).attributes(a,a2).build();
	
	
	public static String language = "lang"; 
	public static String language2 = "lang2"; 
	public static String v = "1.0"; 

}
