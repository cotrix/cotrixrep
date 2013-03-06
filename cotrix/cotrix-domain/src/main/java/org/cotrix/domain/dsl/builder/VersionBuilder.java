package org.cotrix.domain.dsl.builder;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Versioned;

/**
 * Builds version sentences.
 * 
 * @author Fabio Simeoni
 *
 */
public class VersionBuilder<T extends Versioned<T>> {

	private final IdGenerator generator;
	private final T versioned;
	
	public VersionBuilder(IdGenerator generator,T versioned) {
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
