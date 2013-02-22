package org.cotrix.domain.common;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.DeltaObject;
import org.cotrix.domain.traits.Identified;
import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.traits.Named;

/**
 * An {@link Identified}, {@link Named}, {@link Mutable} and {@link Copyable} domain object that can serve as {@link DeltaObject}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public interface DomainObject<T> extends Identified,Named,Copyable<T>,Mutable<T>, DeltaObject {
	

}