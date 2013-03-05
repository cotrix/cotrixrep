package org.cotrix.domain.trait;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.container.Container;

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
	Container<Attribute> attributes();
}
