package org.cotrix.domain.trait;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.Container;
import org.cotrix.domain.primitive.PContainer;

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
	Container<? extends Attribute> attributes();
	
	/**
	 * A private extension of {@link Attributed}.
	 */
	public static interface Private<T extends Private<T>> extends Attributed,Identified.Private<T> {
		
		@Override
		public PContainer<Attribute.Private> attributes();
	}
}
