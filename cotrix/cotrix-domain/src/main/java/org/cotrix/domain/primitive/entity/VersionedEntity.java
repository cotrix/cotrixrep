package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.trait.Versioned;

/**
 * An {@link AttributedEntity} that can be {@link Versioned}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface VersionedEntity<T extends VersionedEntity<T>> extends NamedEntity<T>,AttributedEntity<T>,Versioned<T> {

}