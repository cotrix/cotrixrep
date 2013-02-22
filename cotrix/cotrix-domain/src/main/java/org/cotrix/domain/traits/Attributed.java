package org.cotrix.domain.traits;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.Bag;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Attributed {

	/**
	 * Returns the attributes of this object.
	 * @return the attributes
	 */
	Bag<Attribute> attributes();
}
