package org.cotrix.domain.common;

import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Named;

/**
 * A {@link Container} that may contain multiple objects with the same name.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects
 */
public interface Bag<T extends Named> extends Container<T,Bag<T>> {

	/**
	 * Returns all the contained objects with a given name,
	 * @param name the name
	 * @return the objects with the given name
	 */
	List<T> get(QName name);

}