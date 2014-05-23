package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.attributes.Definition.State;
import org.cotrix.domain.trait.Status;

public final class AttributeMS extends IdentifiedMS implements Attribute.State {

	private String value;
	
	//by default, attribute has 'private' definition
	private Definition.State definition = new DefinitionMS();
	
	public AttributeMS() {
	}
	
	public AttributeMS(String id,Status status) {
		super(id,status);
		type(null);
	}
	
	
	public AttributeMS(Attribute.State state) {
		
		definition(state.definition());
		value(state.value());
	}
	
	@Override
	public State definition() {
		return definition;
	}
	
	@Override
	public void definition(State definition) {
		
		notNull("definition", definition);
		
		this.definition = definition;
	}
	
	public QName name() {
		return definition.name();
	}
	
	public void name(QName name) {
		
		definition.name(name);
	}	
	
	public QName type() {
		return definition.type();
	}
	
	public String value() {
		return value;
	}
	
	public void type(QName type) {
		definition.type(type);
	}
	
	public void value(String value) {	
		this.value = value;
	}

	public String language() {
		return definition.language();
	}

	public void language(String language) {
		definition.language(language);
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
		if (definition == null) {
			if (other.definition() != null)
				return false;
		} else if (!definition.equals(other.definition()))
			return false;
		if (value == null) {
			if (other.value() != null)
				return false;
		} else if (!value.equals(other.value()))
			return false;
		return true;
	}
	


}
