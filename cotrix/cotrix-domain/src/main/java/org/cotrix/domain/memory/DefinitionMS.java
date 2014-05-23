package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.attributes.Text.*;
import static org.cotrix.domain.common.OccurrenceRanges.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.attributes.ValueType;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.trait.Status;

public final class DefinitionMS extends IdentifiedMS implements Definition.State {

	private QName name;
	private QName type;
	private String language;
	private ValueType valueType;
	private Range range;
	
	public DefinitionMS() {
		type=defaultType;
		valueType(freetext);
		range(arbitrarily);
	}
	
	public DefinitionMS(String id,Status status) {
		super(id,status);
	}
	
	
	public DefinitionMS(Definition.State state) {
		
		name(state.name());
		type(state.type());
		language(state.language());
		valueType(state.valueType());
		range(state.range());
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
	
	public void type(QName type) {
		this.type = type;
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
	public Definition.Private entity() {
		return new Definition.Private(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		Definition.State other = (Definition.State) obj;
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
