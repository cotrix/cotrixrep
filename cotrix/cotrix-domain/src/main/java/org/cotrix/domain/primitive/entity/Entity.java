package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.trait.Copyable;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Mutable;

/**
 * An {@link Identified}, {@link Mutable}, and {@link Copyable} object.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of the entity
 */
public interface Entity<T> extends Identified, Mutable<T>, Copyable<T> {

}