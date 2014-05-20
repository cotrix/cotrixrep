package org.cotrix.repository;

import static java.util.Collections.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.ValueType;
import org.cotrix.domain.utils.AttributeTemplate;

/**
 * Summary data about a codelist
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistSummary {

	private final QName name;
	private final int size;
	private final Collection<String> allLanguages = new HashSet<String>();
	private final Collection<QName> allTypes = new HashSet<QName>();
	private final Collection<QName> allNames = new HashSet<QName>();

	private final Map<QName, Map<QName, Set<String>>> codelistFingerprint;
	private final Map<QName, Map<QName, Set<String>>> fingerprint;
	private final Map<QName, Map<QName, Set<String>>> globalFingerprint;

	public CodelistSummary(QName name, int size, Collection<Attribute> attributes, Collection<CodelistLink> links,
			Map<QName, Map<QName, Set<String>>> fingerprint) {

		this.name = name;
		this.size = size;
		this.fingerprint = fingerprint;
		this.codelistFingerprint = new HashMap<>();
		this.globalFingerprint = new HashMap<QName, Map<QName, Set<String>>>();

		addAttributesToFingerprint(this.globalFingerprint, attributes);
		addAttributesToFingerprint(this.codelistFingerprint, attributes);
		
		addLinksToFingerprint(this.globalFingerprint, links);
		
		// build flat globals
		for (QName n : fingerprint.keySet()) {
			allNames.add(n);
			for (Map<QName, Set<String>> typeMap : fingerprint.values()) {
				allTypes.addAll(typeMap.keySet());
				for (Set<String> ls : typeMap.values())
					allLanguages.addAll(ls);
			}
		}

		for (QName n : globalFingerprint.keySet()) {
			allNames.add(n);
			for (Map<QName, Set<String>> typeMap : globalFingerprint.values()) {
				allTypes.addAll(typeMap.keySet());
				for (Set<String> ls : typeMap.values())
					allLanguages.addAll(ls);
			}
		}

	}

	public QName name() {
		return name;
	}

	public int size() {
		return size;
	}

	public Collection<String> allLanguages() {

		return allLanguages;
	}

	public Collection<QName> allTypes() {

		return allTypes;
	}

	public Collection<QName> allNames() {
		return allNames;
	}

	public Collection<String> codeLanguagesFor(QName name, QName type) {
		if (fingerprint.containsKey(name))
			if (fingerprint.get(name).containsKey(type))
				return fingerprint.get(name).get(type);

		return emptySet();
	}
	
	public Collection<String> codelistLanguagesFor(QName name, QName type) {
		if (codelistFingerprint.containsKey(name))
			if (codelistFingerprint.get(name).containsKey(type))
				return codelistFingerprint.get(name).get(type);

		return emptySet();
	}

	public Collection<QName> allTypesFor(QName name) {

		Collection<QName> values = new HashSet<QName>();

		Map<QName, Set<String>> typeMap = globalFingerprint.get(name);

		if (typeMap != null)
			values.addAll(typeMap.keySet());

		typeMap = fingerprint.get(name);

		if (typeMap != null)
			values.addAll(typeMap.keySet());

		return values;
	}

	public Collection<String> allLanguagesFor(QName name, QName type) {

		Collection<String> values = new HashSet<String>();

		if (globalFingerprint.containsKey(name))
			if (globalFingerprint.get(name).containsKey(type))
				values.addAll(globalFingerprint.get(name).get(type));

		if (fingerprint.containsKey(name))
			if (fingerprint.get(name).containsKey(type))
				values.addAll(fingerprint.get(name).get(type));

		return values;
	}

	public Collection<QName> codeNames() {
		return fingerprint.keySet();
	}

	public Collection<QName> codelistNames() {
		return codelistFingerprint.keySet();
	}

	public Collection<QName> codeTypesFor(QName name) {
		return fingerprint.get(name).keySet();
	}

	public Collection<QName> codelistTypesFor(QName name) {
		return codelistFingerprint.get(name).keySet();
	}

	public static void addAttributesToFingerprint(Map<QName, Map<QName, Set<String>>> fingerprint, Iterable<? extends Attribute> attributes) {

		for (Attribute a : attributes)
			addAttributeToFingerprint(fingerprint, a);

	}

	public static void addLinksToFingerprint(Map<QName, Map<QName, Set<String>>> fingerprint, Iterable<? extends CodelistLink> links) {

		for (CodelistLink a : links)
			addLinkToFingerprint(fingerprint, a);

	}

	// helper
	private static void addAttributeToFingerprint(Map<QName, Map<QName, Set<String>>> fingerprint, Attribute a) {

		if (a.type().equals(SYSTEM_TYPE))
			return;

		addTo(typesForName(fingerprint, a.name()), a.type(), a.language());

	}

	// helper
	private static void addLinkToFingerprint(Map<QName, Map<QName, Set<String>>> fingerprint, CodelistLink l) {

		Map<QName, Set<String>> types = typesForName(fingerprint, l.name());

		ValueType type = l.valueType();

		if (type instanceof AttributeLink) {

			AttributeTemplate template = ((AttributeLink) type).template();

			addTo(types, template.type(), template.language());

		}

	}

	private static Map<QName, Set<String>> typesForName(Map<QName, Map<QName, Set<String>>> fingerprint, QName name) {

		Map<QName, Set<String>> types = fingerprint.get(name);

		if (types == null) {

			types = new HashMap<QName, Set<String>>();
			fingerprint.put(name, types);

		}

		return types;

	}

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
