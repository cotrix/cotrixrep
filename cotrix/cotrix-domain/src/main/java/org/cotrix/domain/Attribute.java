package org.cotrix.domain;

import javax.xml.namespace.QName;

import org.cotrix.domain.primitive.entity.NamedEntity;


/**
 * An named and typed attribute for a domain object.
 * @author Fabio Simeoni
 *
 */
public interface Attribute extends NamedEntity<Attribute> {

	/**
	 * Returns the type of the attribute.
	 * @return the type
	 */
	QName type();

	/**
	 * Returns the value of the attribute
	 * @return the value
	 */
	String value();
	

}