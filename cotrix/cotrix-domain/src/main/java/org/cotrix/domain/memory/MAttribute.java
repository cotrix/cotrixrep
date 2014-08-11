package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.Facet;
import org.cotrix.domain.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MAttribute extends MIdentified implements Attribute.Bean {
	
	private static final Logger log = LoggerFactory.getLogger(Attribute.class);
	
	private String value;
	private String note;
	
	//by default, attribute has 'private' definition
	private AttributeDefinition.Bean definition = new MAttrDef(false);
	
	public MAttribute() {
	}
	
	public MAttribute(String id,Status status) {
		super(id,status);
		type(null);
	}
	
	
	public MAttribute(Attribute.Bean state) {
		this(state,new HashMap<String,Object>());
	}

	public MAttribute(Attribute.Bean bean, Map<String,Object> context) {
		
		//attributes preserve identifiers
		super(bean.id());
		
		definition(cloneDefinitionInContext(bean.definition(),context));
		value(bean.value());
		note(bean.note());
	}
	
	
	@Override
	public AttributeDefinition.Bean definition() {
		return definition;
	}
	
	@Override
	public void definition(AttributeDefinition.Bean definition) {
		
		notNull("definition", definition);
		
		this.definition = definition;
		
		this.value(definition.valueType().defaultValue());
	}
	
	public QName qname() {
		return definition.qname();
	}
	
	public void qname(QName name) {
		
		definition.qname(name);
	}	
	
	public QName type() {
		return definition.type();
	}
	
	@Override
	public boolean is(QName name) {
		return definition.is(name);
	}
	
	@Override
	public boolean is(Facet facet) {
		return definition.is(facet);
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
	
	public String note() {
		return note;
	}

	public void note(String note) {
		this.note=note;
	}
	
	@Override
	public Attribute.Private entity() {
		return new Attribute.Private(this);
	}

	
	
	//helper
	private AttributeDefinition.Bean cloneDefinitionInContext(AttributeDefinition.Bean def, Map<String,Object> context) {
		
		if (!def.isShared())
			return new MAttrDef(def);
		
		if (context==null) {
			
			log.error("cannot share definition '{}' during copy, as there is no context",def.qname());
			
			return new MAttrDef(def);
		}
			

		if (!context.containsKey(def.id()))
			throw new AssertionError("application error: definition '"+def.qname()+"' cannot be shared during copy");
		
		return (AttributeDefinition.Bean) context.get(def.id());
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		Attribute.Bean other = (Attribute.Bean) obj;
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
