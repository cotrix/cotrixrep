package org.cotrix.domain.primitive.entity;

import org.cotrix.domain.trait.Copyable;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Mutable;
import org.cotrix.domain.trait.Named;

/**
 * An {@link Identified}, {@link Named}, {@link Mutable} and {@link Copyable} domain object that can serve as {@link Delta}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the object
 */
public interface NamedEntity<T extends NamedEntity<T>> extends Entity<T>,Named {
	
	//to self-copy and self-update, DOs take and return DOs of the same type. hence the 'self' parameterisation.
}