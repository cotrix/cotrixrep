package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;

/**
 * A {@link Named} and {@link Attributed} {@link Entity}.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the entity
 */
public interface NamedEntity<T> extends AttributedEntity<T>, Named {

}