package org.cotrix.repository;

import java.util.Collection;

import javax.xml.namespace.QName;

/**
 * Summary data about a codelist
 * @author Fabio Simeoni
 *
 */
public class CodelistSummary {

	private final QName name;
	private final int size;
	private final Collection<QName> names;
	private final Collection<QName> types;
	private final Collection<String> languages;
	
	public CodelistSummary(QName name,int size,Collection<QName> names,Collection<QName> types, Collection<String> languages) {
		this.name=name;
		this.size=size;
		this.names = names;
		this.types=types;
		this.languages=languages;
	}
	
	public QName name() {
		return name;
	}
	
	public int size() {
		return size;
	}
	
	public Collection<QName> names() {
		return names;
	}
	
	public Collection<String> languages() {
		return languages;
	}
	
	public Collection<QName> types() {
		return types;
	}

	
}
