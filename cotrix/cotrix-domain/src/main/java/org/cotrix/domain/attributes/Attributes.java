package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Utils.*;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;


public class Attributes {

	//preserves insertion order
	private final Map<QName,Attribute> attributes = new LinkedHashMap<QName,Attribute>();
	
	public Attributes() {}
	
	//copy constructor
	public Attributes(Attributes attributes) {
	
		for (Attribute a : attributes.list())
			addAttribute(a);
	}
	
	public Iterable<Attribute> list() {
		return attributes.values();
	}
	
	public boolean hasAttribute(QName name) {
		
		valid(name);
		
		return attributes.containsKey(name);
	}
	
	public Attribute attribute(QName name) throws IllegalStateException {
		
		valid(name);
		
		exists(name);
		
		return attributes.get(name);
	}
	
	public Attribute addAttribute(Attribute attribute) {
		return attributes.put(attribute.name(), attribute);
	}
	
	public Attribute removeAttribute(QName name) {
		
		valid(name);
		
		exists(name);
		
		return attributes.remove(name);
	}

	
	//helper
	private void exists(QName name) throws IllegalStateException {
		if (!attributes.containsKey(name))
			throw new IllegalStateException("unknown attribute "+name);
	}
}
