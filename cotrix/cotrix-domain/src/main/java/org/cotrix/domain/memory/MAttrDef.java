package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Facet;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.values.ValueType;

public final class MAttrDef extends MDescribed implements AttributeDefinition.Bean {

	private QName type;
	private String language;
	private ValueType valueType;
	private Range range;
	private boolean shared;
	
	public MAttrDef() {
		this(true);
	}
	
	public MAttrDef(boolean shared) {
		
		type(defaultType);
		valueType(defaultValueType);
		range(defaultRange);
		shared(shared);
	}
	
	public MAttrDef(String id,Status status) {
		super(id,status);
	}
	
	
	public MAttrDef(AttributeDefinition.Bean other) {
		
		super(other);
		
		type(other.type());
		language(other.language());
		valueType(other.valueType()); //no need to clone: once created, it's immutable.
		range(other.range());
		shared(other.isShared());
	}
	
	void shared(boolean flag) {
		shared=flag;
	}
	
	@Override
	public boolean isShared() {
		return shared;
	}
	
	public QName type() {
		return type;
	}
	
	public void type(QName type) {
		this.type = type;
	}
	
	@Override
	public boolean is(QName name) {
		return type.equals(name);
	}
	
	@Override
	public boolean is(Facet facet) {
		//temporarily only on common defs, supported by default on domain defs
		return !isCommon(qname()) || isCommon(qname(),facet);
	}

	public String language() {
		return language;
	}

	public void language(String language) {
		this.language = language;
	}
	
	public ValueType valueType() {
		return valueType;
	}
	
	public void valueType(ValueType valueType) {
		notNull("link type",valueType);
		this.valueType=valueType;
	}
	
	public Range range() {
		return range;
	}
	
	public void range(Range range) {
		notNull("occurrence range",range);
		this.range=range;
	}
	
	@Override
	public AttributeDefinition.Private entity() {
		return new AttributeDefinition.Private(this);
	}


	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		
		AttributeDefinition.Bean other = (AttributeDefinition.Bean) obj;
		if (language == null) {
			if (other.language() != null)
				return false;
		} else if (!language.equals(other.language()))
			return false;
		if (range == null) {
			if (other.range() != null)
				return false;
		} else if (!range.equals(other.range()))
			return false;
		if (type == null) {
			if (other.type() != null)
				return false;
		} else if (!type.equals(other.type()))
			return false;
		if (valueType == null) {
			if (other.valueType() != null)
				return false;
		} else if (!valueType.equals(other.valueType()))
			return false;
		return true;
	}
	

	

}
