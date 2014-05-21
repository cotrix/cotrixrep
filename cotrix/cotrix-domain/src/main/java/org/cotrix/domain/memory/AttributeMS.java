package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.attributes.AttributeType.State;
import org.cotrix.domain.trait.Status;

public final class AttributeMS extends IdentifiedMS implements Attribute.State {

	private String value;
	private AttributeType.State attributeType;
	
	public AttributeMS() {
		attributeType(new AttributeTypeMS());
	}
	
	public AttributeMS(String id,Status status) {
		super(id,status);
		attributeType(new AttributeTypeMS());
		type(null);
	}
	
	
	public AttributeMS(Attribute.State state) {
		
		attributeType(state.attributeType());
		value(state.value());
	}
	
	@Override
	public State attributeType() {
		return attributeType;
	}
	
	@Override
	public void attributeType(State attributeType) {
		
		notNull("attribute type", attributeType);
		
		this.attributeType = attributeType;
	}
	
	public QName name() {
		return attributeType.name();
	}
	
	public void name(QName name) {
		
		attributeType.name(name);
	}	
	
	public QName type() {
		return attributeType.type();
	}
	
	public String value() {
		return value;
	}
	
	public void type(QName type) {
		attributeType.type(type);
	}
	
	public void value(String value) {	
		this.value = value;
	}

	public String language() {
		return attributeType.language();
	}

	public void language(String language) {
		attributeType.language(language);
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
		Attribute.State other = (Attribute.State) obj;
		if (attributeType == null) {
			if (other.attributeType() != null)
				return false;
		} else if (!attributeType.equals(other.attributeType()))
			return false;
		if (value == null) {
			if (other.value() != null)
				return false;
		} else if (!value.equals(other.value()))
			return false;
		return true;
	}
	


}
