package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.trait.Attributed;

/**
 * An {@link Entity} with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface AttributedEntity<T> extends Entity<T>, Attributed {}