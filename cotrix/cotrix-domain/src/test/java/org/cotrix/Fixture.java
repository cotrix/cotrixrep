package org.cotrix;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitives.BaseBag;
import org.cotrix.domain.primitives.BaseGroup;
import org.cotrix.domain.primitives.DomainObject;
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
	
	
	public static Attribute a = attr("id").name(name).value(value).ofType(type).build();
	public static Attribute a2 = attr("id2").name(name2).value(value2).ofType(type2).build();
	public static Attribute a3 = attr("id3").name(name3).value(value3).ofType(type3).build();
	
	public static Code c = code("id").name(name).and(a).build();
	public static Code c2 = code("id2").name(name2).and(a,a2).build();
	public static Code c3 = code("id3").name(name3).and(a,a2,a3).build();
	
	public static <T extends DomainObject<T>> BaseBag<T> bag(T ... ts) {
		return new BaseBag<T>(asList(ts));
	}
	
	public static <T extends DomainObject<T>> BaseGroup<T> group(T ... ts) {
		return new BaseGroup<T>(asList(ts));
	}
	
	public static BaseBag<Attribute> attributes(Attribute ...attributes) {
		return bag(attributes);
	}
	
	public static BaseGroup<Code> codes(Code ...codes) {
		return group(codes);
	}
	
	public static BaseGroup<Codelist> codelists(Codelist ...lists) {
		return group(lists);
	}
	
	public static String version = "1.0";
	
	public static SimpleVersion no_version = new SimpleVersion();
	
	public static Codelist cl = codelist("id1").name(name).with(c).and(a).version(no_version.value()).build();
	public static Codelist cl2 = codelist("id2").name(name2).with(c,c2).and(a,a2).build();
	
	
	public static String language = "lang"; 
	public static String v = "1.0"; 

}
