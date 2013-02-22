package org.cotrix.domain.common;

import javax.xml.namespace.QName;

/**
 * A {@link Container} that may contain at most a single objects with a given name.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects
 */
public interface Group<T extends DomainObject<T>> extends Container<T,Group<T>> {

	/**
	 * Returns the contained object with a given name, if it exists.
	 * @param name the name
	 * @return the object
	 * @throws IllegalStateException if no contained object has the given name
	 */
	T get(QName name) throws IllegalStateException;

}