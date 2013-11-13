package org.cotrix.repository;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;

/**
 * Summary data about a codelist
 * @author Fabio Simeoni
 *
 */
public class CodelistSummary {

	private final QName name;
	private final int size;
	private final Collection<String> allLanguages = new ArrayList<String>();
	private final Collection<QName> allTypes = new ArrayList<QName>();
	private final Collection<QName> allNames = new ArrayList<QName>();
	
	private final Map<QName,Map<QName,Set<String>>> fingerprint;
	
	

	
	public CodelistSummary(QName name,int size,Collection<Attribute> attributes, Map<QName,Map<QName,Set<String>>> fingerprint) {
		
		this.name=name;
		this.size=size;
		this.fingerprint=fingerprint;
		
		for (Attribute a : attributes) {
			allNames.add(a.name());
			allTypes.add(a.type());
			if (a.language()!=null)
				allLanguages.add(a.language());
		}
		
		for (QName n : fingerprint.keySet())
			allNames.add(n);
		
		for (Map<QName,Set<String>> typeMap : fingerprint.values()) {
			allTypes.addAll(typeMap.keySet());
			for (Set<String> ls : typeMap.values())
				allLanguages.addAll(ls);
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

	public Collection<QName> codeNames() {
		return fingerprint.keySet();
	}
	
	public Collection<QName> codeTypesFor(QName name) {
		return fingerprint.get(name).keySet();
	}
	
	public Collection<String> codeLanguagesFor(QName name,QName type) {
		if (fingerprint.containsKey(name))
			if (fingerprint.get(name).containsKey(type))
					return fingerprint.get(name).get(type);
			
		return emptySet();
	}
}
