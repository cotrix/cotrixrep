package org.cotrix.domain.utils;

import static java.text.DateFormat.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.utils.CodeStatus.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.common.NamedContainer;


//read-only interface over codes with support for systemic attributes
public class ManagedCode implements Code {

	
	public static ManagedCode manage(Code code) {
		return new ManagedCode(code);
	}
	
	
	
	
	private final Code managed;
	
	
	ManagedCode(Code code) {
		this.managed=code;
	}
	
	public Date created() {
		
		String val = lookup(CREATION_TIME.qname());
		
		try {
			return val==null? null : getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	public String originId() {
		
		return lookup(PREVIOUS_VERSION_ID);
		
	}
	
	public QName originName() {
		
		String val = lookup(PREVIOUS_VERSION_NAME);
		
		return val== null ? null : QName.valueOf(val);
		
	}
	
	public Date lastUpdated() {
		
		String val = lookup(UPDATE_TIME.qname());
		
		try {
			return val==null? created():getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	public String lastUpdatedBy() {
		
		String val = lookup(UPDATED_BY.qname());
		
		return val;
	}
	
	public CodeStatus status() {
		
		String val = lookup(STATUS.qname());
		
		if (val==null)
			return VALID;
		
		return CodeStatus.valueOf(val);
	}
	
	
	//helper
	private String lookup(QName name) {
		return managed.attributes().contains(name) ?
			managed.attributes().lookup(name).value():
			null;
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
