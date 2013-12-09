package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.utils.Constants;

public final class AttributeMS extends IdentifiedMS implements Attribute.State {

	private QName name;
	private QName type;
	private String value;
	private String language;
	
	public AttributeMS() {
		type=Constants.DEFAULT_TYPE;
	}
	
	public AttributeMS(String id,Status status) {
		super(id,status);
		type(null);
	}
	
	
	public AttributeMS(Attribute.State state) {
		
		name(state.name());
		type(state.type());
		value(state.value());
		language(state.language());
	}
	
	public QName name() {
		return name;
	}
	
	public void name(QName name) {
		
		valid("name",name);
		
		this.name = name;
	}	
	
	public QName type() {
		return type;
	}
	
	public String value() {
		return value;
	}
	
	public void type(QName type) {
		this.type = type;
	}
	
	public void value(String value) {	
		this.value = value;
	}

	public String language() {
		return language;
	}

	public void language(String language) {		
		this.language = language;
	}
	
	@Override
	public Attribute.Private entity() {
		return new Attribute.Private(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AttributeMS))
			return false;
		AttributeMS other = (AttributeMS) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
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

}
