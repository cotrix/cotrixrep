package org.cotrix.repository;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

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
	private final Map<QName,Map<QName,Set<String>>> fingerprint;
	
	

	
	public CodelistSummary(QName name,int size,Map<QName,Map<QName,Set<String>>> fingerprint) {
		this.name=name;
		this.size=size;
		this.fingerprint=fingerprint;
		
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
	
	public Collection<String> languages() {
		
		return allLanguages;
	}
	
	public Collection<QName> types() {
		
		return allTypes;
	}


	public Collection<QName> names() {
		return fingerprint.keySet();
	}
	
	public Collection<QName> typesFor(QName name) {
		return fingerprint.get(name).keySet();
	}
	
	public Collection<String> languagesFor(QName name,QName type) {
		if (fingerprint.containsKey(name))
			if (fingerprint.get(name).containsKey(type))
					return fingerprint.get(name).get(type);
			
		return emptySet();
	}
}
