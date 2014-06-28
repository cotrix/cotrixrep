package org.cotrix.repository.impl;

import static java.util.Collections.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.utils.AttributeTemplate;

public class Fingerprint {

	private final Map<QName, Map<QName, Set<String>>> data = new HashMap<>();
	
	public Fingerprint addAttributes(Iterable<? extends Attribute> attributes) {
		
		for (Attribute a : attributes)
			if (!a.is(SYSTEM_TYPE))
			  addTo(typesFor(a.name()), a.type(), a.language());
		
		return this;
	}
	
	
	public Fingerprint addDefinitions(Iterable<? extends Definition> definitions) {
		
		for (Definition def : definitions)
			if (!def.is(SYSTEM_TYPE))
				addTo(typesFor(def.name()), def.type(), def.language());
			
		return this;
	}
	
	
	public Fingerprint addLinks(Iterable<? extends CodelistLink> links) {

		for (CodelistLink link : links) {
			
			LinkValueType type = link.valueType();

			if (type instanceof AttributeLink) {
	
					Map<QName, Set<String>> types = typesFor(link.name());
	
					AttributeTemplate template = ((AttributeLink) type).template();
	
					addTo(types, template.type(), template.language());
	
			}
		}
		
		return this;
	}

	
	public Map<QName, Set<String>> typesFor(QName name) {

		Map<QName, Set<String>> types = data.get(name);

		if (types == null) {

			types = new HashMap<QName, Set<String>>();
			data.put(name, types);

		}

		return types;
	}
	
	public Collection<QName> names() {
		return data.keySet();
	}
	
	public Collection<String> languagesFor(QName name, QName type) {
		
		if (data.containsKey(name) && data.get(name).containsKey(type))
				return typesFor(name).get(type);

		return emptySet();
	}
	
	
	// helpers
	
	private static void addTo(Map<QName, Set<String>> types, QName type, String language) {

		Set<String> langForType = types.get(type);

		if (langForType == null) {

			langForType = new HashSet<String>();
			types.put(type, langForType);

		}

		if (language != null)
			langForType.add(language);

	}

}
