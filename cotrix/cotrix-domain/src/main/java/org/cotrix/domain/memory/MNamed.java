package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;


public class MNamed extends MIdentified implements Named.Bean {

	private QName name;
	
	public MNamed() {		
	}

	public MNamed(String id,Status status) {
		super(id,status);
	}
	
	public MNamed(String id) {
		super(id);
	}
	
	public MNamed(Named.Bean other) {	
		
		qname(other.qname());
	}
	
	public MNamed(String id, Named.Bean other) {	
		
		this(id);
		
		qname(other.qname());
	}
	
	public QName qname() {
		return name;
	}
	
	public void qname(QName name) {
		
		valid("name",name);
		
		this.name = name;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Named.Bean))
			return false;
		Named.Bean other = (Named.Bean) obj;
		if (name == null) {
			if (other.qname() != null)
				return false;
		} else if (!name.equals(other.qname()))
			return false;
		return true;
	}	
	
	
}
