package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.attributes.AttributeValueType;
import org.cotrix.domain.common.OccurrenceRange;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.utils.Constants;

public final class AttributeTypeMS extends IdentifiedMS implements AttributeType.State {

	private QName name;
	private QName type;
	private String value;
	private String language;
	private AttributeValueType valueType;
	private OccurrenceRange range;
	
	public AttributeTypeMS() {
		type=Constants.DEFAULT_TYPE;
	}
	
	public AttributeTypeMS(String id,Status status) {
		super(id,status);
		type(null);
	}
	
	
	public AttributeTypeMS(Attribute.State state) {
		
		name(state.name());
		type(state.type());
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

	public String language() {
		return language;
	}

	public void language(String language) {
		this.language = language;
	}
	
	public AttributeValueType valueType() {
		return valueType;
	}
	
	public void valueType(AttributeValueType valueType) {
		notNull("link type",valueType);
		this.valueType=valueType;
	}
	
	public OccurrenceRange range() {
		return range;
	}
	
	public void range(OccurrenceRange range) {
		notNull("occurrence range",range);
		this.range=range;
	}
	
	@Override
	public AttributeType.Private entity() {
		return new AttributeType.Private(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Attribute.State))
			return false;
		Attribute.State other = (Attribute.State) obj;
		if (language == null) {
			if (other.language() != null)
				return false;
		} else if (!language.equals(other.language()))
			return false;
		if (name == null) {
			if (other.name() != null)
				return false;
		} else if (!name.equals(other.name()))
			return false;
		if (type == null) {
			if (other.type() != null)
				return false;
		} else if (!type.equals(other.type()))
			return false;
		if (value == null) {
			if (other.value() != null)
				return false;
		} else if (!value.equals(other.value()))
			return false;
		return true;
	}

}
