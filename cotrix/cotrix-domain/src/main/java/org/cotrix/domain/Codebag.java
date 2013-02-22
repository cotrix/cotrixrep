package org.cotrix.domain;

import org.cotrix.domain.common.Group;
import org.cotrix.domain.common.VersionedObject;


/**
 * A collection of {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codebag extends VersionedObject<Codebag> {

	/**
	 * Returns the code lists in this bag.
	 * @return the lists.
	 */
	Group<Codelist> lists();

}