package org.cotrix.domain.primitives;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * A {@link Container} that may contain multiple objects with the same name.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects
 */
public interface Bag<T extends DomainObject<T>> extends Container<T,Bag<T>> {

	/**
	 * Returns all the contained objects with a given name,
	 * @param name the name
	 * @return the objects with the given name
	 */
	List<T> get(QName name);

}