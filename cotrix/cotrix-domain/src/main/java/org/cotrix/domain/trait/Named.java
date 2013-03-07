package org.cotrix.domain.trait;

import javax.xml.namespace.QName;

/**
 * A named domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Named {

	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	QName name();
	
}
