package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.trait.Versioned;

/**
 * An {@link NamedEntity} that can be {@link Versioned}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface VersionedEntity<T> extends NamedEntity<T>,Versioned<T> {

}