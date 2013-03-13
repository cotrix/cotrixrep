package org.cotrix.domain;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * An named and typed attribute for a domain object.
 * @author Fabio Simeoni
 *
 */
public interface Attribute extends Identified, Named {

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
	

	public interface Private extends Attribute, Identified.Private<Private> {}
}