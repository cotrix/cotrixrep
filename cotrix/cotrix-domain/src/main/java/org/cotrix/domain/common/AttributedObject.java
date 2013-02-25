package org.cotrix.domain.common;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.traits.Attributed;
import org.cotrix.domain.traits.Mutable;

/**
 * A {@link Mutable} {@link DomainObject} with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface AttributedObject<T extends AttributedObject<T>> extends DomainObject<T>, Attributed {}