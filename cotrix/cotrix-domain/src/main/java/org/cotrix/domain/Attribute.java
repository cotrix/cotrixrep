package org.cotrix.domain;

import javax.xml.namespace.QName;

import org.cotrix.domain.primitive.entity.Entity;
import org.cotrix.domain.trait.Named;


/**
 * An named and typed attribute for a domain object.
 * @author Fabio Simeoni
 *
 */
public interface Attribute extends Entity<Attribute>, Named {

	/**
	 * Returns the name of the attribute.
	 * @return the name
	 */
	QName name();
	
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