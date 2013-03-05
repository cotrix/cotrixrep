package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Mutable;

/**
 * A {@link Mutable} {@link NamedEntity} with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface AttributedEntity<T extends AttributedEntity<T>> extends NamedEntity<T>, Attributed {}