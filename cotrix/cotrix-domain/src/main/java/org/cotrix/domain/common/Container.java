package org.cotrix.domain.common;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.traits.Named;

/**
 * A {@link Copyable} and {@link Mutable} container of {@link Named} objects that can serve as a {@link Delta}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects
 * @param <C> the self-type of the container 
 */
interface Container<T extends DomainObject<T>, C extends Container<T,C>> extends Copyable<C>, Iterable<T>, Mutable<C> {

	/**
	 * Returns the number of contained objects.
	 * @return the number of entities
	 */
	int size();

	/**
	 * Returns <code>true</code> if the container has at least an object with a given name.
	 * @param name the name
	 * @return <code>true</code> if the container has at least an object with the given name
	 */
	boolean contains(QName name);

	/**
	 * Returns <code>true</code> if the container contains a given object.
	 * @param object the object
	 * @return <code>true</code> if the container contains the given object
	 */
	boolean contains(T object);

}