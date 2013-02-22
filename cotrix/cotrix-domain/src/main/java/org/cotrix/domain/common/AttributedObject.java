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
//joins the traits of domain objects that have attributes, hence become mutable
public interface AttributedObject<T> extends DomainObject<T>, Attributed {}