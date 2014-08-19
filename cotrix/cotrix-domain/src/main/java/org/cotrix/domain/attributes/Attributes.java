package org.cotrix.domain.attributes;

import static java.text.DateFormat.*;
import static org.cotrix.common.CommonUtils.*;

import java.text.ParseException;
import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Named;


public class Attributes extends Container.Private<Attribute.Private,Attribute.Bean> {

	public Attributes() {
		super();
	}
	
	public Attributes(Container.Bean<Attribute.Bean> beans) {
		super(beans);
	}
	
	public Attributes(Attribute.Private ... attrs) {
		super(attrs);
	}
	
	public Date dateOf(Named named) {
		
		String val = valueOf(named);
		
		try {
			return val==null? null : getDateTimeInstance().parse(val);
		}
		catch(ParseException e) {
			throw unchecked(e);
		}
	}
	
	public QName nameOf(Named named) {
		
		String val = valueOf(named);
		
		try {
			return val==null? null : QName.valueOf(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	
	public String valueOf(Named def) {
		
		Attribute a  = getFirst(def);
		
		return a==null ? null : a.value();
	
	}
}
