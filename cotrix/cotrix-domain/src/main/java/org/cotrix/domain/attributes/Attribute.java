package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;

//no reason to mutate this
public class Attribute implements Named, Copyable<Attribute> {

	
	private final QName name;
	private final QName type;
	private final String value;
	
	public Attribute(QName name,QName type,String value) throws IllegalArgumentException {
		
		valid(name);
		this.name=name;
		
		if (type!=null)
			valid(type);
		
		this.type=type;
		
		valid("value",value);
		this.value=value;
		
	}
	
	public Attribute(QName name,String value) throws IllegalArgumentException {
		
		this(name,null,value);
		
	}
	
	public QName name() {
		return name;
	}
	
	public QName type() {
		return type;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", value=" + value + (type==null?"":", type=" + type)+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Attribute copy() {
		return new Attribute(name(),type(),value());
	}
}
