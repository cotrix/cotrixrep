package org.cotrix;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.versions.SimpleVersion;

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
	
	
	public static Attribute a = a("id").with(name).and(value).ofType(type).build();
	public static Attribute a2 = a("id2").with(name2).and(value2).ofType(type2).build();
	public static Attribute a3 = a("id3").with(name3).and(value3).ofType(type3).build();
	
	public static BaseBag<Attribute> attributes(Attribute ...attributes) {
		return new BaseBag<Attribute>(asList(attributes));
	}
	
	public static Code c = code("id").with(name).and(a).build();
	public static Code c2 = code("id2").with(name2).and(a,a2).build();
	public static Code c3 = code("id3").with(name3).and(a,a2,a3).build();
	
	public static BaseGroup<Code> emptyCodes = new BaseGroup<Code>();

	public static SimpleVersion no_version = new SimpleVersion();
	
	public static Codelist cl = codelist("id1").with(name).with(c).and(a).version(no_version.value()).build();
	public static Codelist cl2 = codelist("id2").with(name2).with(c,c2).and(a,a2).build();
	
	public static BaseGroup<Codelist> emptyLists = new BaseGroup<Codelist>();
	
	
	public static String language = "lang"; 
	public static String v = "1.0"; 

}
