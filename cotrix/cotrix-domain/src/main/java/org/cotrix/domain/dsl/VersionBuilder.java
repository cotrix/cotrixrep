package org.cotrix.domain.dsl;

import org.cotrix.domain.traits.Versioned;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Builds version sentences.
 * 
 * @author Fabio Simeoni
 *
 */
public class VersionBuilder<T extends Versioned<T>> {

	private final IdGenerator generator;
	private final T versioned;
	
	VersionBuilder(IdGenerator generator,T versioned) {
		this.versioned=versioned;
		this.generator=generator;
	}
	
	/**
	 * Returns the new object's version.
	 * @param version the version
	 * @return the the new object's version 
	 */
	public T as(String version) {
		return versioned.bump(generator, version);
	}
}
