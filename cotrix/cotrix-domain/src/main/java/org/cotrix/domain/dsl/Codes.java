package org.cotrix.domain.dsl;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.LanguageAttribute;
import org.cotrix.domain.codes.Code;
import org.cotrix.domain.codes.Codebag;
import org.cotrix.domain.codes.Codelist;

public class Codes {

	public static QName q(String local) {
		return new QName(local);
	}
	
	public static QName q(String ns, String local) {
		return new QName(ns,local);
	}
	
	public static Attribute a(QName name,String value) {
		return new Attribute(name,value);
	}
	
	public static Attribute a(QName name, QName type, String value) {
		return new Attribute(name,type,value);
	}
	
	public static LanguageAttribute a(QName name, String value,String language) {
		return new LanguageAttribute(name,value,language);
	}
	
	public static LanguageAttribute a(QName name, QName type,String value,String language) {
		return new LanguageAttribute(name,type,value,language);
	}
	
	public static Code ascode(String name) {
		return ascode(q(name));
	}
	
	public static Code ascode(QName name) {
		return new Code(name);
	}
	
	public static CodeBuilder code(QName name) {
		return new CodeBuilder(name);
	}
	
	public static CodeBuilder code(Code code) {
		return new CodeBuilder(code);
	}
	
	public static CodelistBuilder codelist(QName name) {
		return new CodelistBuilder(name);
	}
	
	public static CodelistBuilder codelist(Codelist list) {
		return new CodelistBuilder(list);
	}
	
	public static CodebagBuilder codebag(QName name) {
		return new CodebagBuilder(name);
	}
	
	public static CodebagBuilder codebag(Codebag bag) {
		return new CodebagBuilder(bag);
	}
}
