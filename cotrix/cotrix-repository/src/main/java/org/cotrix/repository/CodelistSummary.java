package org.cotrix.repository;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.impl.Fingerprint;

/**
 * Summary data about a codelist
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistSummary {

	private final QName name;
	private final int size;
	
	private final Fingerprint codelistFingerprint;
	private final Fingerprint codeFingerprint;
	
	public CodelistSummary(Codelist list) {

		this.name = list.qname();
		this.size = list.codes().size();
		
		this.codeFingerprint = new Fingerprint()
								.addDefinitions(list.definitions());
		
		this.codelistFingerprint = new Fingerprint().addAttributes(list.attributes());
		
		
	}

	public QName name() {
		return name;
	}

	public int size() {
		return size;
	}

	
	public Collection<QName> allNames() {
		Collection<QName> names = new HashSet<>(codeFingerprint.attributeNames());
		names.addAll(codelistFingerprint.attributeNames());
		return names;
	}

	public Collection<String> codeLanguagesFor(QName name, QName type) {
		
		return codeFingerprint.languagesFor(name, type);
	}
	
	public Collection<String> codelistLanguagesFor(QName name, QName type) {
		
		return codelistFingerprint.languagesFor(name, type);
	}

	public Collection<QName> codeNames() {
		return codeFingerprint.attributeNames();
	}

	public Collection<QName> codelistNames() {
		return codelistFingerprint.attributeNames();
	}

	public Collection<QName> codeTypesFor(QName name) {
		return codeFingerprint.typesFor(name).keySet();
	}

	public Collection<QName> codelistTypesFor(QName name) {
		return codelistFingerprint.typesFor(name).keySet();
	}
}
