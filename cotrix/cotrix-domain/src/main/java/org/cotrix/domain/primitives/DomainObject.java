package org.cotrix.domain.primitives;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Identified;
import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.traits.Named;

/**
 * An {@link Identified}, {@link Named}, {@link Mutable} and {@link Copyable} domain object that can serve as {@link Delta}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the object
 */
public interface DomainObject<T extends DomainObject<T>> extends Identified,Named,Copyable<T>,Mutable<T> {
	
	//to self-copy and self-update, DOs take and return DOs of the same type. hence the 'self' parameterisation.
}