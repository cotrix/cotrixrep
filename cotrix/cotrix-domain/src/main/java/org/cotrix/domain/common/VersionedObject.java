package org.cotrix.domain.common;

import org.cotrix.domain.traits.Versioned;

/**
 * An {@link AttributedObject} that can be {@link Versioned}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface VersionedObject<T> extends AttributedObject<T>,Versioned<T> {

}