package org.cotrix.domain.codes;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.attributes.Attributes;


public class Code implements Attributed {

	private final String value;
	private final QName name;
	private final Attributes attributes;
	
	
	public Code(QName name,String value) {
		this(name,value,new Attributes());
	}
	
	public Code(QName name,String value,Attributes attributes) {
		this.name=name;
		this.value = value;
		this.attributes=attributes;
	}
	
	public Attributes attributes() {
		return attributes;
	}
	
	public QName name() {
		return name;
	}
	
	public String value() {
		return value;
	}

}
