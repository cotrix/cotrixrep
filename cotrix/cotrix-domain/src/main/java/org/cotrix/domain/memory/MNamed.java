package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Status;
import org.cotrix.domain.trait.Named;


public class MNamed extends MIdentified implements Named.Bean {

	//not needed for virtually named beans, but hey we do not have real traits here.
	private QName name;
	
	
	
	public MNamed() {		
	}

	public MNamed(String id,Status status) {
		super(id,status);
	}
	

	public MNamed(String id) {
		super(id);
	}
	
	//copy
	public MNamed(Named.Bean other) {	
		
		super();
		
		copy(other);
	}
	
	//copy but with given id
	public MNamed(String id, Named.Bean other) {	
		
		super(id);
		
		copy(other);
	}
	
	//shared copy constructor logic
	private void copy(Named.Bean other) {
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
		if (qname() == null) {
			if (other.qname() != null)
				return false;
		} else if (!qname().equals(other.qname()))
			return false;
		return true;
	}	
	
	
}
