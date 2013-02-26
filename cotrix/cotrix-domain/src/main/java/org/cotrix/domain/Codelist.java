package org.cotrix.domain;

import org.cotrix.domain.primitives.Group;
import org.cotrix.domain.primitives.VersionedObject;


/**
 * A list of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends VersionedObject<Codelist> {

	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	Group<Code> codes();

}