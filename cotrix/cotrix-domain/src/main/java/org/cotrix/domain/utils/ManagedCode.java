package org.cotrix.domain.utils;

import static java.text.DateFormat.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.NamedContainer;

public class ManagedCode implements Code {

	
	public static ManagedCode manage(Code code) {
		return new ManagedCode(code);
	}
	
	
	
	
	private final Code managed;
	
	
	ManagedCode(Code code) {
		this.managed=code;
	}
	
	
	public Date created() {
		
		String val = resolve(CREATION_TIME,null);
		
		try {
			return val==null?null:getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	public String originId() {
		
		return resolve(PREVIOUS_VERSION_ID,null);
		
	}
	
	public QName originName() {
		
		String val = resolve(PREVIOUS_VERSION_NAME,null);
		return val==null?null:QName.valueOf(val);
		
	}
	
	public Date lastUpdated() {
		
		String val = resolve(UPDATE_TIME,null);
		
		try {
			return val==null? created():getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	public boolean invalid() {
		
		String val = resolve(INVALID,null);
		return val==null? false: Boolean.valueOf(val);
	}
	
	
	//helper
	private String resolve(QName name, String deflt) {
		return managed.attributes().contains(name) ?
			managed.attributes().lookup(name).value():
			deflt;
	}
	
	
	//delegates
	
	@Override
	public String id() {
		return managed.id();
	}

	@Override
	public NamedContainer<? extends Attribute> attributes() {
		return managed.attributes();
	}

	@Override
	public QName name() {
		return managed.name();
	}

	@Override
	public NamedContainer<? extends Codelink> links() {
		return managed.links();
	}

	
}
