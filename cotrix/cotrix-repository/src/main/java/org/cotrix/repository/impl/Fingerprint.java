package org.cotrix.repository.impl;

import static java.util.Collections.*;
import static org.cotrix.domain.utils.DomainConstants.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;

public class Fingerprint {

	private final Map<QName, Map<QName, Set<String>>> data = new HashMap<>();
	
	public Fingerprint addAttributes(Iterable<? extends Attribute> attributes) {
		
		for (Attribute a : attributes)
			if (!a.is(SYSTEM_TYPE))
			  addAttributeTo(typesFor(a.qname()), a.type(), a.language());
		
		return this;
	}
	
	
	public Fingerprint addDefinitions(Iterable<? extends AttributeDefinition> definitions) {
		
		for (AttributeDefinition def : definitions)
			if (!def.is(SYSTEM_TYPE))
				addAttributeTo(typesFor(def.qname()), def.type(), def.language());
			
		return this;
	}
	
	
	public Collection<QName> attributeNames() {
		return data.keySet();
	}

	public Map<QName, Set<String>> typesFor(QName name) {

		Map<QName, Set<String>> types = data.get(name);

		if (types == null) {

			types = new HashMap<QName, Set<String>>();
			data.put(name, types);

		}

		return types;
	}
	
	public Collection<String> languagesFor(QName name, QName type) {
		
		if (data.containsKey(name) && data.get(name).containsKey(type))
				return typesFor(name).get(type);

		return emptySet();
	}

	
	
	
	
	
	// helpers
	
	private static void addAttributeTo(Map<QName, Set<String>> types, QName type, String language) {

		Set<String> langForType = types.get(type);

		if (langForType == null) {

			langForType = new HashSet<String>();
			types.put(type, langForType);

		}

		if (language != null)
			langForType.add(language);

	}

}
