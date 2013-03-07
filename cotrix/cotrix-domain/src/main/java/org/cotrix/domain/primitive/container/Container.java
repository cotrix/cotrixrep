package org.cotrix.domain.primitive.container;


/**
 * An immutable, typed container.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects 
 */
public interface Container<T> extends Iterable<T> {

	/**
	 * Returns the number of objects in this container.
	 * @return the number of entities
	 */
	int size();

}
