package org.cotrix.domain.managed;

import static java.text.DateFormat.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;

import java.util.Date;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.trait.Attributed;

public abstract class ManagedEntity<T extends Attributed> {


	private T managed;
	
	ManagedEntity() {}

	ManagedEntity(T entity) {
		set(entity);
	}
	
	public T managed() {
		return managed;
	}
	
	public void set(T entity) {
		this.managed=entity;
	}
	
	public Date created() {
		
		String val = lookup(CREATION_TIME);
		
		try {
			return val==null? null : getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	
	public Date lastUpdated() {
		
		String val = lookup(UPDATE_TIME);
		
		try {
			return val==null? created():getDateTimeInstance().parse(val);
		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}
	
	
	public String lastUpdatedBy() {
		
		String val = lookup(UPDATED_BY);
		
		return val;
	}

	
	public String originId() {
		
		return lookup(PREVIOUS_VERSION_ID);
		
	}
	
	public QName originName() {
		
		String val = lookup(PREVIOUS_VERSION_NAME);
		
		return val== null ? null : QName.valueOf(val);
		
	}
	
	public boolean has(CommonDefinition def) {
		return attribute(def)!=null;
	}
	
	public boolean hasno(CommonDefinition def) {
		return attribute(def)==null;
	}
	
	/**
	 * Returns the string value of an attribute with a given common definition, if one exists.
	 * @param def the definition
	 * @return the string value of the attribute, or <code>null</code> if no such attribute exists.
	 * @throws IllegalStateException if there are two or more attributes with the given definition.
	 */
	public String lookup(CommonDefinition def) throws IllegalStateException {
		Attribute a  = attribute(def);
		return a==null?null:a.value();
	}
	
	/**
	 * Returns the attribute with a given common definition, if one exists.
	 * @param def the definition
	 * @return the attribute, or <code>null</code> if no such attribute exists.
	 * @throws IllegalStateException if there are two or more attributes with the given definition.
	 */
	public Attribute attribute(CommonDefinition def) {
		return managed().attributes().contains(def) ?
			managed().attributes().getFirst(def):
			null;
	}
}
