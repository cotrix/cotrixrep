package org.cotrix.domain;

import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.entity.VersionedEntity;


/**
 * A collection of {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codebag extends VersionedEntity<Codebag> {

	/**
	 * Returns the code lists in this bag.
	 * @return the lists.
	 */
	Container<Codelist> lists();

}