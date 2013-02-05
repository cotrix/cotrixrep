package org.cotrix.domain.codes;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Named;
import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.attributes.Attributes;


//immutable value-object
public class Code implements Attributed,Named {

	private final String value;
	private final QName name;
	private final Attributes attributes;
	
	
	public Code(QName name,String value) {
		this(name,value,new Attributes());
	}
	
	public Code(QName name,String value,Attributes attributes) {
		
		valid(name);
		this.name=name;
		
		valid("value",value);
		this.value = value;
		
		notNull("attributes", attributes);
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
