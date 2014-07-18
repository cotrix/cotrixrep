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
import org.cotrix.domain.utils.LinkTemplate;

public class Fingerprint {

	private final Map<QName, Map<QName, Set<String>>> data = new HashMap<>();
	private final Map<QName,Map<QName, Set<QName>>> linkdata = new HashMap<>();
	
	public Fingerprint addAttributes(Iterable<? extends Attribute> attributes) {
		
		for (Attribute a : attributes)
			if (!a.is(SYSTEM_TYPE))
			  addAttributeTo(typesFor(a.qname()), a.type(), a.language());
		
		return this;
	}
	
	
	public Fingerprint addDefinitions(Iterable<? extends Definition> definitions) {
		
		for (Definition def : definitions)
			if (!def.is(SYSTEM_TYPE))
				addAttributeTo(typesFor(def.qname()), def.type(), def.language());
			
		return this;
	}
	
	
	public Fingerprint addLinks(Iterable<? extends CodelistLink> links) {

		for (CodelistLink link : links) {

			LinkTemplate template = new LinkTemplate(link);
			
			addLinkTo(targetsFor(link.qname()),template.target(),template.anchor());

		}
		
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

	
	
	
	
	
	public Collection<QName> linkNames() {
		return linkdata.keySet();
	}
	
	public Map<QName, Set<QName>> targetsFor(QName name) {

		Map<QName, Set<QName>> targets = linkdata.get(name);

		if (targets == null) {

			targets = new HashMap<QName, Set<QName>>();
			linkdata.put(name, targets);

		}

		return targets;
	}
	
	public Collection<String> anchorsFor(QName name, QName target) {
		
		if (linkdata.containsKey(name) && linkdata.get(name).containsKey(target))
				return typesFor(name).get(target);

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

	
	private static void addLinkTo(Map<QName, Set<QName>> targets, QName target, QName anchor) {

		Set<QName> anchorsForTarget = targets.get(target);

		if (anchorsForTarget == null) {

			anchorsForTarget = new HashSet<QName>();
			targets.put(target, anchorsForTarget);

		}

		if (anchorsForTarget != null)
			anchorsForTarget.add(anchor);

	}

}
